package com.library.management.common.exception;

/**
 * Base exception class for all library-related exceptions
 * 
 * @author Library Management System
 * @version 2.0
 */
public class LibraryException extends RuntimeException {
    private String errorCode;

    public LibraryException(String message) {
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibraryException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public LibraryException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
