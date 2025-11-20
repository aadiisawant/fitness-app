package com.fitness.userservice.services;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.models.User;
import com.fitness.userservice.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserResponse createUser(RegisterRequest data){

        if(userRepo.existsByEmail(data.getEmail())){
            log.info("Email already exists!!", data.getEmail());
            User existingUser = userRepo.findByEmail(data.getEmail());
            UserResponse userResponse = new UserResponse();
            userResponse.setKeyCloakId(existingUser.getKeyCloakId());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            return userResponse;
        }

        User user = new User();
        user.setKeyCloakId(data.getKeyCloakId());
        user.setEmail(data.getEmail());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setPassword(data.getPassword());

        User savedUser = userRepo.save(user);
        log.info("User Created.");
        UserResponse userResponse = new UserResponse();
        userResponse.setKeyCloakId(savedUser.getKeyCloakId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        return userResponse;
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
}
