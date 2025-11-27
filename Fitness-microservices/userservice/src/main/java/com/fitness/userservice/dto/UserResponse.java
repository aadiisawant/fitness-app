package com.fitness.userservice.dto;

import com.fitness.userservice.enums.Gender;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {

    private String id;
    private String keyCloakId;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Integer age;
    private Double height;
    private Gender gender;
    private Double latestWeight;
    private Double bmi;
    private Double bodyFatPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
