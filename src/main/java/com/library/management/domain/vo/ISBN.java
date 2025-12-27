package com.library.management.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * ISBN Value Object - Represents and validates ISBN numbers
 * Supports both ISBN-10 and ISBN-13 formats
 * 
 * @author Library Management System
 * @version 2.0
 */
public class ISBN {
    private static final Pattern ISBN_10_PATTERN = Pattern.compile("^\\d{9}[\\dX]$");
    private static final Pattern ISBN_13_PATTERN = Pattern.compile("^\\d{13}$");
    private static final Pattern ISBN_FORMATTED_PATTERN = Pattern.compile("^\\d{3}-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d{1}$");

    private final String value;

    private ISBN(String value) {
        this.value = value;
    }

    /**
     * Creates an ISBN from a string, validating the format
     */
    public static ISBN of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }

        String cleaned = value.replaceAll("[^\\dX]", "");
        
        if (!isValid(cleaned)) {
            throw new IllegalArgumentException("Invalid ISBN format: " + value);
        }

        return new ISBN(cleaned);
    }

    /**
     * Creates an ISBN without validation (for database reads)
     */
    public static ISBN ofUnchecked(String value) {
        return new ISBN(value);
    }

    /**
     * Validates ISBN format
     */
    public static boolean isValid(String isbn) {
        if (isbn == null) return false;
        
        String cleaned = isbn.replaceAll("[^\\dX]", "");
        
        if (ISBN_10_PATTERN.matcher(cleaned).matches()) {
            return validateISBN10(cleaned);
        } else if (ISBN_13_PATTERN.matcher(cleaned).matches()) {
            return validateISBN13(cleaned);
        }
        
        return false;
    }

    private static boolean validateISBN10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (isbn.charAt(i) - '0') * (10 - i);
        }
        char lastChar = isbn.charAt(9);
        sum += (lastChar == 'X') ? 10 : (lastChar - '0');
        return sum % 11 == 0;
    }

    private static boolean validateISBN13(String isbn) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = isbn.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = isbn.charAt(12) - '0';
        return (10 - (sum % 10)) % 10 == checkDigit;
    }

    public String getValue() {
        return value;
    }

    public boolean isISBN10() {
        return value.length() == 10;
    }

    public boolean isISBN13() {
        return value.length() == 13;
    }

    /**
     * Returns formatted ISBN with hyphens
     */
    public String formatted() {
        if (isISBN13()) {
            return String.format("%s-%s-%s-%s-%s",
                value.substring(0, 3),
                value.substring(3, 4),
                value.substring(4, 9),
                value.substring(9, 12),
                value.substring(12, 13));
        } else if (isISBN10()) {
            return String.format("%s-%s-%s-%s",
                value.substring(0, 1),
                value.substring(1, 5),
                value.substring(5, 9),
                value.substring(9, 10));
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ISBN isbn = (ISBN) o;
        return Objects.equals(value, isbn.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
