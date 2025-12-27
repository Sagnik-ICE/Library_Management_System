package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Password Security Utility
 * Handles password hashing and verification using BCrypt
 */
public class PasswordUtil {
    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static final int DEFAULT_COST = 12;

    /**
     * Hashes a plain text password using BCrypt
     * 
     * @param plainPassword The plain text password
     * @return Hashed password string
     */
    public static String hashPassword(String plainPassword) {
        try {
            int cost = ConfigManager.getInstance().getBcryptRounds();
            String hashedPassword = BCrypt.withDefaults().hashToString(cost, plainPassword.toCharArray());
            logger.debug("Password hashed successfully");
            return hashedPassword;
        } catch (Exception e) {
            logger.error("Error hashing password", e);
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    /**
     * Verifies a plain text password against a hashed password
     * 
     * @param plainPassword The plain text password to verify
     * @param hashedPassword The hashed password to compare against
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
            boolean isValid = result.verified;
            logger.debug("Password verification result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error verifying password", e);
            return false;
        }
    }

    /**
     * Checks if a password meets minimum security requirements
     * 
     * @param password The password to check
     * @return true if password meets requirements
     */
    public static boolean meetsSecurityRequirements(String password) {
        if (password == null || password.length() < Constants.MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0);

        // Password must have at least 3 of the 4 character types
        int criteriaCount = (hasUpper ? 1 : 0) + (hasLower ? 1 : 0) + (hasDigit ? 1 : 0) + (hasSpecial ? 1 : 0);
        return criteriaCount >= 3;
    }

    /**
     * Gets password strength description
     * 
     * @param password The password to evaluate
     * @return Strength description (Weak, Medium, Strong)
     */
    public static String getPasswordStrength(String password) {
        if (password == null || password.length() < Constants.MIN_PASSWORD_LENGTH) {
            return "Weak - Too short";
        }

        int score = 0;
        if (password.length() >= 12) score++;
        if (password.chars().anyMatch(Character::isUpperCase)) score++;
        if (password.chars().anyMatch(Character::isLowerCase)) score++;
        if (password.chars().anyMatch(Character::isDigit)) score++;
        if (password.chars().anyMatch(ch -> "!@#$%^&*()_+-=[]{}|;:,.<>?".indexOf(ch) >= 0)) score++;

        if (score <= 2) return "Weak";
        if (score <= 3) return "Medium";
        return "Strong";
    }

    /**
     * Migrates old plain text password to BCrypt hash
     * Used for upgrading existing user accounts
     * 
     * @param plainPassword Plain text password from old system
     * @return BCrypt hashed password
     */
    public static String migratePassword(String plainPassword) {
        logger.info("Migrating password to BCrypt hash");
        return hashPassword(plainPassword);
    }
}
