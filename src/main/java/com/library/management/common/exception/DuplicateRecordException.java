package com.library.management.common.exception;

/**
 * DuplicateRecordException - Thrown when duplicate record is detected
 * 
 * @author Library Management System
 * @version 2.0
 */
public class DuplicateRecordException extends LibraryException {
    
    public DuplicateRecordException(String entityName, String field, String value) {
        super("DUPLICATE_RECORD", 
            String.format("%s with %s '%s' already exists", entityName, field, value));
    }

    public DuplicateRecordException(String message) {
        super("DUPLICATE_RECORD", message);
    }
}
