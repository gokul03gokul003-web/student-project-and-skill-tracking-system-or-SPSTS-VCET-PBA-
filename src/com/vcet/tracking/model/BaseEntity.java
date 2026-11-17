package com.vcet.tracking.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Base Entity providing common audit metadata and primary identification.
 * Demonstrates Unit 2: Abstract Classes, Inheritance, and Encapsulation.
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String createdAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public BaseEntity(String createdAt) {
        this.createdAt = (createdAt != null) ? createdAt : 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public abstract String getEntityIdentifier();
}
