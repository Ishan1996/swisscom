package com.swisscom.project.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.swisscom.project.model.ServiceModel;

@Component
public class ServiceCache {
    private final Map<String, ServiceModel> cache = new ConcurrentHashMap<>();

    public Optional<ServiceModel> get(String id) {
        return Optional.ofNullable(cache.get(id));
    }

    public void put(ServiceModel service) {
        cache.put(service.getId(), service);
    }

    public void update(ServiceModel service) {
        cache.put(service.getId(), service);
    }

    public void delete(String id) {
        cache.remove(id);
    }
}

