package com.swisscom.project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swisscom.project.model.ServiceModel;
import com.swisscom.project.service.ProjectService;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/services")
public class ProjectController {

    private final ProjectService serviceRepository;

    public ProjectController(ProjectService serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @PostMapping
    public ResponseEntity<ServiceModel> createService(@RequestBody ServiceModel service) {
        if (service == null || service.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        ServiceModel savedService = serviceRepository.createService(service);
        return ResponseEntity.ok(savedService);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ServiceModel> updateService(@RequestBody ServiceModel service, @PathVariable String id) {
        if (service == null || service.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        ServiceModel updatedService = serviceRepository.updateService(service, id);
        return ResponseEntity.ok(updatedService);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        serviceRepository.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceModel> getService(@PathVariable String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Optional<ServiceModel> service = serviceRepository.getService(id);
        if (service.isPresent()) {
            return ResponseEntity.ok(service.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<ServiceModel>> getAllServices() {
        List<ServiceModel> services = serviceRepository.getAllServices();
        return ResponseEntity.ok(services);
    }
    
}
