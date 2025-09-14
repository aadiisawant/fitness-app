package com.fitness.activityservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/demo")
public class DiscoveryClients {

    @Autowired
    private DiscoveryClient discoveryClient;

    // Returns list of all registered service IDs
    @GetMapping("/discovery-test")
    public List<String> discoveryTest() {
        return discoveryClient.getServices();
    }

    // Returns list of instances (URI) of "userservice"
    @GetMapping("/discovery-test/userservice-instances")
    public List<String> userserviceInstances() {
        List<ServiceInstance> instances = discoveryClient.getInstances("USERSERVICE");

        if (instances.isEmpty()) {
            return List.of("No instances found for userservice");
        }

        return instances.stream()
                .map(ServiceInstance::getUri)
                .map(URI::toString)
                .collect(Collectors.toList());
    }
}
