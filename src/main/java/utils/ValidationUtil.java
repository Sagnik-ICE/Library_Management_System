package utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Input Validation Utility
 * Provides centralized validation for all user inputs
 */
public class ValidationUtil {
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,15}$");
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]+$");

    /**
     * Validates if a string is not null and not empty
     */
    public static boolean isNotEmpty(String value) {
        return StringUtils.isNotBlank(value);
    }

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EmailValidator.getInstance().isValid(email);
    }

    /**
     * Validates phone number (10-15 digits)
     */
    public static boolean isValidPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.replaceAll("[\\s-]", "")).matches();
    }

    /**
     * Validates ISBN format
     */
    public static boolean isValidISBN(String isbn) {
        if (StringUtils.isBlank(isbn)) {
            return true; // ISBN is optional
        }
        return ISBN_PATTERN.matcher(isbn.replaceAll("[\\s-]", "")).matches();
    }

    /**
     * Validates password strength
     */
    public static boolean isValidPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        return password.length() >= Constants.MIN_PASSWORD_LENGTH && 
               password.length() <= Constants.MAX_PASSWORD_LENGTH;
    }

    /**
     * Validates username format
     */
    public static boolean isValidUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return false;
        }
        return username.length() >= Constants.MIN_USERNAME_LENGTH &&
               username.length() <= Constants.MAX_USERNAME_LENGTH &&
               ALPHANUMERIC_PATTERN.matcher(username).matches();
    }

    /**
     * Validates name (alphabets and spaces only)
     */
    public static boolean isValidName(String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        return name.length() <= Constants.MAX_NAME_LENGTH &&
               NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates numeric input
     */
    public static boolean isValidNumber(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates positive integer
     */
    public static boolean isValidPositiveInteger(String value) {
        if (!isValidNumber(value)) {
            return false;
        }
        try {
            int num = Integer.parseInt(value);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates price (positive decimal)
     */
    public static boolean isValidPrice(String price) {
        if (!isValidNumber(price)) {
            return false;
        }
        try {
            double num = Double.parseDouble(price);
            return num >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates string length
     */
    public static boolean isValidLength(String value, int maxLength) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        return value.length() <= maxLength;
    }

    /**
     * Sanitizes user input to prevent SQL injection
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Remove dangerous characters
        return input.replaceAll("[<>\"';]", "").trim();
    }

    /**
     * Validates required field with custom error message
     */
    public static String validateRequired(String value, String fieldName) {
        if (!isNotEmpty(value)) {
            return fieldName + " is required";
        }
        return null;
    }

    /**
     * Comprehensive validation for email with error message
     */
    public static String validateEmail(String email) {
        if (!isNotEmpty(email)) {
            return "Email is required";
        }
        if (!isValidEmail(email)) {
            return "Invalid email format";
        }
        return null;
    }

    /**
     * Comprehensive validation for phone with error message
     */
    public static String validatePhone(String phone) {
        if (!isNotEmpty(phone)) {
            return "Phone number is required";
        }
        if (!isValidPhone(phone)) {
            return "Invalid phone number (10-15 digits required)";
        }
        return null;
    }
}
