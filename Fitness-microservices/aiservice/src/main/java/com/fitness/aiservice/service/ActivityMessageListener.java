package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendations;
import com.fitness.aiservice.repo.RecommendationsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final RecommendationsRepo recommendationsRepo;
    private final ActivityAIService activityAIService;
    @KafkaListener(topics = "${kafka.topic.name}", groupId = "activity-processor-group")
    public void processActivity(Activity activity){
        log.info("Received Activity for processing: {}",activity.getKeyCloakId());
        Recommendations recommendations = activityAIService.generateRecommendations(activity);
        log.info("Recommendations are fetched from AI...");
        recommendationsRepo.save(recommendations);
        log.info("Recommendations saved.");
    }

}
