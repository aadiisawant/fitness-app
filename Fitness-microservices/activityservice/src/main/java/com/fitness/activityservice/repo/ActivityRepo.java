package com.fitness.activityservice.repo;

import com.fitness.activityservice.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepo extends MongoRepository<Activity, String> {
    Activity findByUserId(String userId);
}
