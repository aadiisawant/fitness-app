package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repo.ActivityRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {

    private ActivityRepo activityRepo;
    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        Activity savedActivity = activityRepo.save(activity);

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse res = new ActivityResponse();
        res.setUserId(activity.getUserId());
        res.setType(activity.getType());
        res.setDuration(activity.getDuration());
        res.setCaloriesBurned(activity.getCaloriesBurned());
        res.setStartTime(activity.getStartTime());
        res.setAdditionalMetrics(activity.getAdditionalMetrics());
        res.setCreatedAt(activity.getCreatedAt());
        res.setUpdatedAt(activity.getUpdatedAt());

        return res;
    }

    public ActivityResponse getActivity(String userId) {
        Activity activity = activityRepo.findByUserId(userId);
        return mapToResponse(activity);
    }
}
