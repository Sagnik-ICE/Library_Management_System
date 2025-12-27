package com.library.management.domain.vo;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * PhoneNumber Value Object - Represents and validates phone numbers
 * 
 * @author Library Management System
 * @version 2.0
 */
public class PhoneNumber {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9]{10,15}$");
    private static final Pattern INDIAN_MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");

    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static PhoneNumber of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }

        String cleaned = value.replaceAll("[\\s()-]", "");
        if (!isValid(cleaned)) {
            throw new IllegalArgumentException("Invalid phone number format: " + value);
        }

        return new PhoneNumber(cleaned);
    }

    public static PhoneNumber ofUnchecked(String value) {
        return new PhoneNumber(value);
    }

    public static boolean isValid(String phoneNumber) {
        if (phoneNumber == null) return false;
        String cleaned = phoneNumber.replaceAll("[\\s()-]", "");
        return PHONE_PATTERN.matcher(cleaned).matches();
    }

    public static boolean isValidIndianMobile(String phoneNumber) {
        if (phoneNumber == null) return false;
        String cleaned = phoneNumber.replaceAll("[\\s()-]", "");
        return INDIAN_MOBILE_PATTERN.matcher(cleaned).matches();
    }

    public String getValue() {
        return value;
    }

    public String formatted() {
        if (value.length() == 10) {
            return String.format("(%s) %s-%s", 
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6));
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
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
