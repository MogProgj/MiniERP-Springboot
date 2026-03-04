package com.mogproj.minierp.common.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityType, Long id) {
        super(entityType + " not found with id: " + id);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
