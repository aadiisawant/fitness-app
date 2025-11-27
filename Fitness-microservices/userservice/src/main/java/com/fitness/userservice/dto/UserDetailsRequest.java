package com.fitness.userservice.dto;

import com.fitness.userservice.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDetailsRequest {
    private LocalDate dateOfBirth;
    private Double height;
    private Gender gender;
    private Double weight;
}
