-- Create Database
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- Table: books
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    author VARCHAR(100),
    quantity INT
);

-- Table: members
CREATE TABLE members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contact VARCHAR(20),
    status ENUM('active', 'inactive') DEFAULT 'active'
);
ALTER TABLE members AUTO_INCREMENT = 1;


-- Table: users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user') NOT NULL,
    status VARCHAR(20) DEFAULT 'active',
    member_id INT,
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Table: issued_books
CREATE TABLE issued_books (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    issue_date DATE NOT NULL,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);
ALTER TABLE books AUTO_INCREMENT = 1;


-- Table: returned_books
CREATE TABLE returned_books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    member_id INT,
    issue_date DATE,
    return_date DATE,
    fine DECIMAL(10,2),
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (member_id) REFERENCES members(id)
);

-- Table: fines
CREATE TABLE fines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    member_id INT,
    book_id INT,
    amount DECIMAL(10, 2),
    paid BOOLEAN DEFAULT FALSE,
    fine_date DATE,
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Fix 'status' ENUM in users
ALTER TABLE users MODIFY COLUMN status ENUM('active', 'inactive') DEFAULT 'active';

-- (Optional) Auto-increment reset for consistent IDs
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE issued_books AUTO_INCREMENT = 1;
ALTER TABLE returned_books AUTO_INCREMENT = 1;
ALTER TABLE fines AUTO_INCREMENT = 1;

