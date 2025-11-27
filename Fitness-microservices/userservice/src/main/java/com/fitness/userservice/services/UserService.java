package com.fitness.userservice.services;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserDetailsRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.enums.Gender;
import com.fitness.userservice.models.User;
import com.fitness.userservice.models.WeightLog;
import com.fitness.userservice.repo.UserRepo;
import com.fitness.userservice.repo.WeightLogRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private WeightLogRepo weightLogRepo;

    public UserResponse createUser(RegisterRequest data){

        if(userRepo.existsByEmail(data.getEmail())){
            log.info("Email already exists!!", data.getEmail());
            return mapToResponse(userRepo.findByEmail(data.getEmail()));
        }

        User user = new User();
        user.setKeyCloakId(data.getKeyCloakId());
        user.setEmail(data.getEmail());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setPassword(data.getPassword());

        User savedUser = userRepo.save(user);
        log.info("User Created.");
        return mapToResponse(savedUser);
    }

    public UserResponse getUserProfile(String userId) {
        log.info("Finding user by ID....");
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not found!"));
        log.info("user found.");
        UserResponse userResponse = new UserResponse();
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;

    }

    public Boolean existByUserId(String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return userRepo.existsByKeyCloakId(userId);
    }

    public UserResponse updateUserProfile(String keycloakId, UserDetailsRequest req) {
        User user = (User) userRepo.findByKeyCloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.getDateOfBirth() != null){
            user.setDateOfBirth(req.getDateOfBirth());
            log.info("DOB added.");
        }
        if (req.getHeight() != null) {
            user.setHeight(req.getHeight());
            log.info("Added Height in CM");
        }
        if (req.getGender() != null) {
            user.setGender(req.getGender());
            log.info("Gender added.");
        }

        userRepo.save(user);

        if (req.getWeight() != null) {
            WeightLog log = new WeightLog();
            log.setUser(user);
            log.setKeyCloakId(keycloakId);
            log.setWeight(req.getWeight());
            weightLogRepo.save(log);
        }

        return getHealthReport(keycloakId);
    }

    public UserResponse getHealthReport(String keycloakId) {
        User user = (User) userRepo.findByKeyCloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = mapToResponse(user);

        // Get latest weight
        Double weight = weightLogRepo
                .findTopByUserKeyCloakIdOrderByLoggedAtDesc(keycloakId)
                .map(WeightLog::getWeight)
                .orElse(null);

        response.setLatestWeight(weight);

        // Compute derived values
        if (weight != null && user.getHeight() != null) {

            double bmi = calculateBMI(weight, user.getHeight());
            response.setBmi(bmi);

            if (user.getDateOfBirth() != null && user.getGender() != null) {
                int age = calculateAge(user.getDateOfBirth());
                response.setAge(age);

                double fat = calculateBodyFat(bmi, age, user.getGender());
                response.setBodyFatPercentage(fat);
            }
        }

        return response;
    }
    private UserResponse mapToResponse(User user) {
        UserResponse res = new UserResponse();
        res.setId(user.getId());
        res.setKeyCloakId(user.getKeyCloakId());
        res.setEmail(user.getEmail());
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setHeight(user.getHeight());
        res.setGender(user.getGender());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        return res;
    }

    // Utilities
    private int calculateAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    private double calculateBMI(double weight, double heightCm) {
        double hm = heightCm / 100.0;
        return weight / (hm * hm);
    }

    private double calculateBodyFat(double bmi, int age, Gender gender) {
        if (gender == Gender.MALE)
            return 1.20 * bmi + 0.23 * age - 16.2;
        else
            return 1.20 * bmi + 0.23 * age - 5.4;
    }
}
