package com.library.management.common.exception;

/**
 * BusinessException - Thrown when business rule is violated
 * 
 * @author Library Management System
 * @version 2.0
 */
public class BusinessException extends LibraryException {
    
    public BusinessException(String message) {
        super("BUSINESS_RULE_VIOLATION", message);
    }

    public BusinessException(String errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(String message, Throwable cause) {
        super("BUSINESS_RULE_VIOLATION", message, cause);
    }
}
