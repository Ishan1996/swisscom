package com.swisscom.project.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class OwnerModel {
    @Id
    private String id;
    private String name;
    private String accountNumber;
    private int level;
}
