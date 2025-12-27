package com.library.management.common.exception;

import java.util.List;

/**
 * ValidationException - Thrown when validation fails
 * 
 * @author Library Management System
 * @version 2.0
 */
public class ValidationException extends LibraryException {
    private List<String> validationErrors;

    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
    }

    public ValidationException(List<String> validationErrors) {
        super("VALIDATION_ERROR", "Validation failed: " + String.join(", ", validationErrors));
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
