package utils;

import java.awt.*;

/**
 * Application Constants
 * Centralized location for all magic numbers and strings
 */
public final class Constants {
    
    // Prevent instantiation
    private Constants() {}

    // Application Info
    public static final String APP_NAME = "Library Management System";
    public static final String APP_VERSION = "2.0.0";
    
    // UI Colors
    public static final Color HEADER_BLUE = new Color(51, 153, 255);
    public static final Color HEADER_ORANGE = new Color(255, 153, 51);
    public static final Color HEADER_GREEN = new Color(76, 175, 80);
    public static final Color HEADER_STEEL_BLUE = new Color(70, 130, 180);
    public static final Color HEADER_PURPLE = new Color(156, 39, 176);
    
    // UI Dimensions
    public static final int DEFAULT_WINDOW_WIDTH = 800;
    public static final int DEFAULT_WINDOW_HEIGHT = 600;
    public static final int HEADER_HEIGHT = 50;
    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 40;
    
    // UI Fonts
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 18);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 12);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    
    // Database Status
    public static final String STATUS_ACTIVE = "active";
    public static final String STATUS_INACTIVE = "inactive";
    
    // User Roles
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_USER = "user";
    
    // Gender Options
    public static final String[] GENDERS = {"Male", "Female", "Other"};
    
    // Book Genres
    public static final String[] BOOK_GENRES = {
        "Fiction", "Non-Fiction", "Science", "Technology", 
        "History", "Biography", "Children", "Romance", 
        "Mystery", "Fantasy", "Horror", "Self-Help",
        "Education", "Reference", "Poetry", "Drama"
    };
    
    // Validation Constraints
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_PHONE_LENGTH = 20;
    public static final int MAX_ISBN_LENGTH = 20;
    public static final int MAX_TITLE_LENGTH = 200;
    
    // Business Rules
    public static final int DEFAULT_LOAN_PERIOD_DAYS = 14;
    public static final double DEFAULT_FINE_PER_DAY = 10.0;
    public static final int GRACE_PERIOD_DAYS = 0;
    
    // File Paths
    public static final String IMAGE_DIRECTORY = "Images/";
    public static final String BOOK_IMAGE_DIRECTORY = "book images/";
    public static final String LOG_DIRECTORY = "logs/";
    
    // Date Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // Messages
    public static final String MSG_SUCCESS = "Operation completed successfully";
    public static final String MSG_ERROR = "An error occurred";
    public static final String MSG_DB_ERROR = "Database error occurred";
    public static final String MSG_VALIDATION_ERROR = "Please check your input";
    public static final String MSG_LOGIN_FAILED = "Invalid username or password";
    public static final String MSG_UNAUTHORIZED = "You don't have permission";
    
    // Dialog Titles
    public static final String TITLE_SUCCESS = "Success";
    public static final String TITLE_ERROR = "Error";
    public static final String TITLE_WARNING = "Warning";
    public static final String TITLE_CONFIRM = "Confirmation";
    
    // SQL Queries - Template names (actual queries in DAO classes)
    public static final int MAX_QUERY_TIMEOUT = 30;
    public static final int DEFAULT_FETCH_SIZE = 100;
}
