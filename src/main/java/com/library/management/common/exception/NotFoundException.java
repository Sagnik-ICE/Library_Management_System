package com.library.management.common.exception;

/**
 * NotFoundException - Thrown when entity is not found
 * 
 * @author Library Management System
 * @version 2.0
 */
public class NotFoundException extends LibraryException {
    
    public NotFoundException(String entityName, Long id) {
        super("NOT_FOUND", String.format("%s with ID %d not found", entityName, id));
    }

    public NotFoundException(String entityName, String identifier) {
        super("NOT_FOUND", String.format("%s with identifier '%s' not found", entityName, identifier));
    }

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
