package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager - Loads and manages application properties
 * Singleton pattern for centralized configuration access
 */
public class ConfigManager {
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Unable to find application.properties");
                throw new RuntimeException("Configuration file not found");
            }
            properties.load(input);
            logger.info("Application properties loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading application properties", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer property: {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    public double getDoubleProperty(String key, double defaultValue) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            logger.warn("Invalid double property: {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    // Specific getters for common properties
    public String getDatabaseUrl() {
        return getProperty("db.url");
    }

    public String getDatabaseUsername() {
        return getProperty("db.username");
    }

    public String getDatabasePassword() {
        return getProperty("db.password");
    }

    public String getDatabaseDriver() {
        return getProperty("db.driver");
    }

    public int getHikariMaxPoolSize() {
        return getIntProperty("hikari.maximum.pool.size", 10);
    }

    public int getHikariMinIdle() {
        return getIntProperty("hikari.minimum.idle", 5);
    }

    public long getHikariConnectionTimeout() {
        return getIntProperty("hikari.connection.timeout", 30000);
    }

    public double getFineRatePerDay() {
        return getDoubleProperty("app.fine.rate.per.day", 10.0);
    }

    public int getDefaultLoanPeriodDays() {
        return getIntProperty("app.default.loan.period.days", 14);
    }

    public int getBcryptRounds() {
        return getIntProperty("security.bcrypt.rounds", 12);
    }
}
