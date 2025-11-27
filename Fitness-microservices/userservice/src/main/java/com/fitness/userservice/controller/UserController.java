package com.fitness.userservice.controller;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserDetailsRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/register-user")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.createUser(registerRequest));
    }

    @PostMapping("/profile")
    public ResponseEntity<UserResponse> updateUserDetails(@RequestHeader("X-User-ID") String keycloakId, @RequestBody UserDetailsRequest request) {
        log.info("updating user profile...");
        return ResponseEntity.ok(userService.updateUserProfile(keycloakId, request));
    }

    @GetMapping("/health")
    public ResponseEntity<UserResponse> getHealthReport(@RequestHeader("X-User-ID") String keycloakId) {
        return ResponseEntity.ok(userService.getHealthReport(keycloakId));
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/validate/{userId}")
    public ResponseEntity<Boolean> validateUser(@PathVariable String userId){
        log.info("Resquest for validating the user id: {}",userId);
        return ResponseEntity.ok(userService.existByUserId(userId));
    }
}
