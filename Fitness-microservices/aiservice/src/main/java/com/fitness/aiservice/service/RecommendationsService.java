package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Recommendations;
import com.fitness.aiservice.repo.RecommendationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationsService {

    @Autowired
    private RecommendationsRepo recommendationsRepo;

    public List<Recommendations> getUserRecommendations(String userId) {
        return recommendationsRepo.findByUserId(userId);
    }

    public Recommendations getActivityRecommendations(String activityId) {
        return recommendationsRepo.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("Recommendations not found! for Activity: "+activityId));
    }
}
