package com.swisscom.project.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.swisscom.project.model.ServiceModel;

@Repository
public interface ServiceRepository extends MongoRepository<ServiceModel, String> {
} 