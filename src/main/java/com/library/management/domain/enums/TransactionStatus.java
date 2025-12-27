package com.library.management.domain.enums;

/**
 * Transaction Status Enum
 * 
 * @author Library Management System
 * @version 2.0
 */
public enum TransactionStatus {
    ISSUED("Issued", "Book is currently issued"),
    RETURNED("Returned", "Book has been returned"),
    OVERDUE("Overdue", "Book return is overdue"),
    LOST("Lost", "Book is reported lost"),
    DAMAGED("Damaged", "Book returned damaged");

    private final String displayName;
    private final String description;

    TransactionStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static TransactionStatus fromString(String status) {
        if (status == null) return ISSUED;
        
        for (TransactionStatus s : values()) {
            if (s.name().equalsIgnoreCase(status) || 
                s.displayName.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return ISSUED;
    }
}
