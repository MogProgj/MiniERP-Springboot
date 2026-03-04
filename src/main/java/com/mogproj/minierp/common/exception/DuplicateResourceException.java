package com.mogproj.minierp.common.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String field, Object value) {
        super("Duplicate value for field '" + field + "': " + value);
    }
}
