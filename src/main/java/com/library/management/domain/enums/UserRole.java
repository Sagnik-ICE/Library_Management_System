package com.library.management.domain.enums;

/**
 * User Role Enum
 * 
 * @author Library Management System
 * @version 2.0
 */
public enum UserRole {
    ADMIN("Administrator", "Full system access"),
    LIBRARIAN("Librarian", "Can manage books and transactions"),
    USER("User", "Limited access to view and borrow books");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isLibrarian() {
        return this == LIBRARIAN;
    }

    public boolean canManageBooks() {
        return this == ADMIN || this == LIBRARIAN;
    }

    public boolean canManageMembers() {
        return this == ADMIN;
    }

    public static UserRole fromString(String role) {
        if (role == null) return USER;
        
        for (UserRole r : values()) {
            if (r.name().equalsIgnoreCase(role) || 
                r.displayName.equalsIgnoreCase(role)) {
                return r;
            }
        }
        return USER;
    }
}
