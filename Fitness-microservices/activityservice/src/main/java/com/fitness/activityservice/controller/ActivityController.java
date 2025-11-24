package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.service.ActivityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
@AllArgsConstructor
@Slf4j
public class ActivityController {
    private ActivityService activityService;

    @PostMapping()
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest activityRequest,  @RequestHeader("X-User-ID") String userId){
        log.info("In Activity Controller....");
        activityRequest.setKeyCloakId(userId);
        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<List<ActivityResponse>> getActivityByUserId(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(activityService.getActivity(userId));
    }

}
