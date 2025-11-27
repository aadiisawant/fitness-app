package com.fitness.userservice.repo;

import com.fitness.userservice.models.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeightLogRepo extends JpaRepository<WeightLog, String> {
    Optional<WeightLog> findTopByUserKeyCloakIdOrderByLoggedAtDesc(String keycloakId);
}
