-- ============================================================================
-- LIBRARY MANAGEMENT SYSTEM - DATABASE SCHEMA
-- File: library_db.sql
-- Purpose: Create the complete database structure for the Library Management System
-- Version: 2.0
-- Created: December 2024
-- ============================================================================

-- Drop existing database to start fresh
DROP DATABASE IF EXISTS library_db;

-- Create database with UTF-8 support
CREATE DATABASE library_db 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE library_db;

-- ============================================================================
-- TABLE 1: BOOKS
-- Purpose: Stores complete book inventory with detailed metadata
-- ============================================================================
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    unique_book_id VARCHAR(20) UNIQUE NOT NULL COMMENT 'Custom unique identifier (e.g., BK001)',
    isbn VARCHAR(20) COMMENT 'International Standard Book Number',
    title VARCHAR(100) NOT NULL COMMENT 'Book title',
    author VARCHAR(100) NOT NULL COMMENT 'Author name',
    genre VARCHAR(50) COMMENT 'Book category/genre',
    publisher VARCHAR(100) COMMENT 'Publisher name',
    price DECIMAL(10, 2) COMMENT 'Book price',
    date_received DATE COMMENT 'Date when book was added to library',
    quantity INT NOT NULL DEFAULT 0 COMMENT 'Total available copies',
    description TEXT COMMENT 'Book description/summary',
    cover_path VARCHAR(255) COMMENT 'Path to book cover image',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Indexes for performance
    INDEX idx_title (title),
    INDEX idx_author (author),
    INDEX idx_isbn (isbn),
    INDEX idx_genre (genre),
    INDEX idx_unique_book_id (unique_book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book inventory management';

-- ============================================================================
-- TABLE 2: MEMBERS
-- Purpose: Stores library member/patron information
-- ============================================================================
CREATE TABLE members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    unique_member_id VARCHAR(20) UNIQUE NOT NULL COMMENT 'Custom unique identifier (e.g., MEM001)',
    first_name VARCHAR(50) NOT NULL COMMENT 'Member first name',
    last_name VARCHAR(50) NOT NULL COMMENT 'Member last name',
    father_name VARCHAR(100) COMMENT 'Father name',
    mother_name VARCHAR(100) COMMENT 'Mother name',
    date_of_birth DATE COMMENT 'Date of birth',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT 'Email address (must be unique)',
    contact_number VARCHAR(20) COMMENT 'Phone/mobile number',
    address TEXT COMMENT 'Residential address',
    gender ENUM('Male', 'Female', 'Other') COMMENT 'Gender',
    profile_pic VARCHAR(255) COMMENT 'Path to profile picture',
    status ENUM('active', 'inactive') DEFAULT 'active' COMMENT 'Account status',
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Registration date',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Indexes for performance
    INDEX idx_email (email),
    INDEX idx_unique_member_id (unique_member_id),
    INDEX idx_status (status),
    INDEX idx_name (first_name, last_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Library member records';

-- ============================================================================
-- TABLE 3: USERS
-- Purpose: Authentication and authorization for system access
-- ============================================================================
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT 'Login username',
    password VARCHAR(100) NOT NULL COMMENT 'Hashed password (BCrypt)',
    role ENUM('admin', 'user') NOT NULL COMMENT 'User role/permission level',
    status ENUM('active', 'inactive') DEFAULT 'active' COMMENT 'Account status',
    member_id INT NULL COMMENT 'Link to members table (NULL for admin users)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL COMMENT 'Last successful login time',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign key constraint
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_username (username),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User authentication and authorization';

-- ============================================================================
-- TABLE 4: ISSUED_BOOKS
-- Purpose: Tracks book issuance and return status
-- ============================================================================
CREATE TABLE issued_books (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL COMMENT 'Reference to books table',
    member_id INT NOT NULL COMMENT 'Reference to members table',
    issue_date DATE NOT NULL COMMENT 'Date when book was issued',
    return_date DATE NULL COMMENT 'Date when book was returned (NULL = still issued)',
    actual_return_date DATE NULL COMMENT 'Actual return date (may differ from expected)',
    status VARCHAR(20) DEFAULT 'issued' COMMENT 'Issue status (issued, returned, overdue, lost)',
    notes TEXT COMMENT 'Additional notes or comments',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    
    -- Indexes for performance
    INDEX idx_issue_date (issue_date),
    INDEX idx_return_date (return_date),
    INDEX idx_status (status),
    INDEX idx_member_book (member_id, book_id),
    INDEX idx_book_id (book_id),
    INDEX idx_member_id (member_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Book issuance tracking';

-- ============================================================================
-- TABLE 5: RETURNED_BOOKS
-- Purpose: Complete history of all book returns with fine details
-- ============================================================================
CREATE TABLE returned_books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT COMMENT 'Reference to books table',
    member_id INT COMMENT 'Reference to members table',
    issue_date DATE COMMENT 'Original issue date',
    return_date DATE COMMENT 'Actual return date',
    fine DECIMAL(10,2) DEFAULT 0.00 COMMENT 'Fine amount calculated (if any)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints (SET NULL allows historical data even if book/member deleted)
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE SET NULL,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_return_date (return_date),
    INDEX idx_member_id (member_id),
    INDEX idx_book_id (book_id),
    INDEX idx_fine (fine)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Historical record of book returns';

-- ============================================================================
-- TABLE 6: FINES
-- Purpose: Tracks outstanding and paid fines for overdue books
-- ============================================================================
CREATE TABLE fines (
    id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT COMMENT 'Reference to members table',
    book_id INT COMMENT 'Reference to books table',
    fine_amount DECIMAL(10, 2) NOT NULL COMMENT 'Fine amount in currency',
    fine_date DATE NOT NULL COMMENT 'Date when fine was recorded',
    paid BOOLEAN DEFAULT FALSE COMMENT 'Payment status',
    payment_date DATE NULL COMMENT 'Date when fine was paid',
    notes TEXT COMMENT 'Additional notes about the fine',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE SET NULL,
    
    -- Indexes for performance
    INDEX idx_member_id (member_id),
    INDEX idx_book_id (book_id),
    INDEX idx_paid (paid),
    INDEX idx_fine_date (fine_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Fine management and tracking';

-- ============================================================================
-- DEFAULT DATA: ADMIN USER
-- Create default admin account (password will be hashed on first login)
-- ============================================================================
INSERT INTO users (username, password, role, status, member_id) 
VALUES ('admin', 'admin123', 'admin', 'active', NULL);

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================
SELECT '========================================' as '';
SELECT 'DATABASE SCHEMA CREATED SUCCESSFULLY!' as Status;
SELECT '========================================' as '';

SELECT 'Tables Created:' as Info;
SHOW TABLES;

SELECT '========================================' as '';
SELECT 'Default Admin User Created:' as Info;
SELECT id, username, role, status FROM users WHERE role = 'admin';

SELECT '========================================' as '';
SELECT 'Database Ready for Use!' as Status;
SELECT 'Next Step: Run test_db.sql to add sample data' as Instruction;
SELECT '========================================' as '';
