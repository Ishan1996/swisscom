package com.swisscom.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.swisscom.project.model.ServiceModel;

import com.swisscom.project.repository.ServiceCache;
import com.swisscom.project.repository.ServiceRepository;

@Service
public class ProjectService {
    
    private final ServiceRepository repository;
    private final ServiceCache cache;

    public ProjectService(ServiceRepository repository, ServiceCache cache) {
        this.repository = repository;
        this.cache = cache;
    }

    public Optional<ServiceModel> getService(String id) {
        Optional<ServiceModel> cachedService = cache.get(id);
        if (cachedService.isPresent()) {
            return cachedService;
        }

        Optional<ServiceModel> service = repository.findById(id);
        if (!service.isPresent()) {
            cache.put(service.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found with id: " + id);
        }
        return service;
    }

    public ServiceModel updateService(ServiceModel updated, String id) {
        if (!updated.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid service data or ID mismatch");
        }
        if (!serviceExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found with id: " + id);
        }
        ServiceModel saved = repository.save(updated);
        cache.update(saved);
        return saved;
    }

    public ServiceModel createService(ServiceModel service) {
        ServiceModel saved = repository.save(service);
        cache.put(saved);
        return saved;
    }

    public void deleteService(String id) {
        if (!serviceExists(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found with id: " + id);
        }
        repository.deleteById(id);
        cache.delete(id);
    }

    public List<ServiceModel> getAllServices() {
        return repository.findAll();
    }

    public boolean serviceExists(String id) {
        return repository.existsById(id);
    }
}
