package com.library.management.domain.enums;

/**
 * Member Status Enum
 * 
 * @author Library Management System
 * @version 2.0
 */
public enum MemberStatus {
    ACTIVE("Active", "Member can borrow books"),
    INACTIVE("Inactive", "Member account is inactive"),
    SUSPENDED("Suspended", "Member temporarily suspended"),
    BLOCKED("Blocked", "Member permanently blocked");

    private final String displayName;
    private final String description;

    MemberStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public boolean canBorrowBooks() {
        return this == ACTIVE;
    }

    public static MemberStatus fromString(String status) {
        if (status == null) return ACTIVE;
        
        for (MemberStatus s : values()) {
            if (s.name().equalsIgnoreCase(status) || 
                s.displayName.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return ACTIVE;
    }
}
