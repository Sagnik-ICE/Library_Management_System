-- ============================================================================
-- LIBRARY MANAGEMENT SYSTEM - TEST DATA
-- File: test_db.sql
-- Purpose: Insert sample data for testing and development
-- Prerequisites: Must run library_db.sql first
-- Version: 2.0
-- ============================================================================

USE library_db;

-- ============================================================================
-- SAFETY CHECK
-- ============================================================================
SELECT 'Preparing to insert test data...' as Status;

-- ============================================================================
-- CLEAR EXISTING TEST DATA (preserves admin user)
-- ============================================================================
SET FOREIGN_KEY_CHECKS = 0;

-- Clear dependent tables first
TRUNCATE TABLE fines;
TRUNCATE TABLE returned_books;
TRUNCATE TABLE issued_books;

-- Clear main data tables
DELETE FROM users WHERE role = 'user';
TRUNCATE TABLE members;
TRUNCATE TABLE books;

SET FOREIGN_KEY_CHECKS = 1;

-- Reset auto-increment values
ALTER TABLE books AUTO_INCREMENT = 1;
ALTER TABLE members AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 2; -- Start from 2 (admin is 1)
ALTER TABLE issued_books AUTO_INCREMENT = 1;
ALTER TABLE returned_books AUTO_INCREMENT = 1;
ALTER TABLE fines AUTO_INCREMENT = 1;

-- ============================================================================
-- INSERT SAMPLE BOOKS (10 Books across different genres)
-- ============================================================================
INSERT INTO books (unique_book_id, isbn, title, author, genre, publisher, price, date_received, quantity, description) VALUES
-- Programming & Computer Science
('BK001', '978-0-13-468599-1', 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Robert C. Martin', 'Programming', 'Prentice Hall', 45.99, '2024-01-15', 5, 'Even bad code can function. But if code isn''t clean, it can bring a development organization to its knees.'),
('BK002', '978-0-201-63361-0', 'Design Patterns: Elements of Reusable Object-Oriented Software', 'Erich Gamma', 'Programming', 'Addison-Wesley', 54.99, '2024-01-20', 3, 'Capturing a wealth of experience about the design of object-oriented software, four top-notch designers present a catalog of simple and succinct solutions.'),
('BK003', '978-0-13-235088-4', 'The C Programming Language', 'Brian W. Kernighan', 'Programming', 'Prentice Hall', 39.99, '2024-02-01', 4, 'Written by the developers of C, this new version helps readers keep up with the finalized ANSI standard for C.'),
('BK004', '978-0-13-110362-7', 'The Pragmatic Programmer: Your Journey To Mastery', 'Andrew Hunt', 'Programming', 'Addison-Wesley', 49.99, '2024-02-10', 3, 'Illustrates the best approaches and major pitfalls of many different aspects of software development.'),

-- Web Development
('BK005', '978-0-596-52068-7', 'JavaScript: The Good Parts', 'Douglas Crockford', 'Web Development', 'O Reilly Media', 35.99, '2024-02-15', 6, 'Most programming languages contain good and bad parts, but JavaScript has more than its share of the bad.'),
('BK006', '978-1-59327-928-8', 'Python Crash Course: A Hands-On, Project-Based Introduction', 'Eric Matthes', 'Programming', 'No Starch Press', 42.99, '2024-03-01', 8, 'A fast-paced, thorough introduction to programming with Python that will have you writing programs, solving problems, and making things that work in no time.'),

-- Computer Science & Algorithms
('BK007', '978-0-262-03384-8', 'Introduction to Algorithms', 'Thomas H. Cormen', 'Computer Science', 'MIT Press', 89.99, '2024-03-05', 2, 'Some books on algorithms are rigorous but incomplete; others cover masses of material but lack rigor. Introduction to Algorithms uniquely combines rigor and comprehensiveness.'),
('BK008', '978-0-13-235088-1', 'Database System Concepts', 'Abraham Silberschatz', 'Database', 'McGraw-Hill', 79.99, '2024-03-10', 4, 'Presents the fundamental concepts of database management in an intuitive manner geared toward allowing students to begin working with databases as quickly as possible.'),

-- Software Engineering
('BK009', '978-0-13-468796-4', 'Software Engineering: A Practitioner''s Approach', 'Roger S. Pressman', 'Software Engineering', 'McGraw-Hill', 69.99, '2024-03-15', 3, 'For almost four decades, Software Engineering: A Practitioner''s Approach has been the world''s leading textbook in software engineering.'),
('BK010', '978-0-321-60043-6', 'Artificial Intelligence: A Modern Approach', 'Stuart Russell', 'Artificial Intelligence', 'Pearson', 94.99, '2024-03-20', 2, 'The long-anticipated revision of this best-selling text offers the most comprehensive, up-to-date introduction to the theory and practice of artificial intelligence.');

-- ============================================================================
-- INSERT SAMPLE MEMBERS (5 Members with complete details)
-- ============================================================================
INSERT INTO members (unique_member_id, first_name, last_name, father_name, mother_name, date_of_birth, email, contact_number, address, gender, status) VALUES
('MEM001', 'John', 'Doe', 'Michael Doe', 'Sarah Doe', '1995-05-15', 'john.doe@email.com', '9876543210', '123 Main Street, Downtown, Metro City, 110001', 'Male', 'active'),
('MEM002', 'Jane', 'Smith', 'Robert Smith', 'Emily Smith', '1998-08-22', 'jane.smith@email.com', '9876543211', '456 Oak Avenue, Uptown, Metro City, 110002', 'Female', 'active'),
('MEM003', 'Alex', 'Johnson', 'David Johnson', 'Lisa Johnson', '2000-03-10', 'alex.johnson@email.com', '9876543212', '789 Pine Road, Suburbs, Metro City, 110003', 'Male', 'active'),
('MEM004', 'Sarah', 'Williams', 'James Williams', 'Maria Williams', '1997-11-05', 'sarah.williams@email.com', '9876543213', '321 Elm Street, Central District, Metro City, 110004', 'Female', 'active'),
('MEM005', 'Test', 'User', 'Test Father', 'Test Mother', '1999-01-01', 'testuser@library.com', '9999999999', '999 Test Street, Test Area, Metro City, 110099', 'Male', 'active');

-- ============================================================================
-- INSERT USER ACCOUNTS (4 Regular Users + 1 Admin already exists)
-- Password will be hashed on first login by the application
-- ============================================================================
INSERT INTO users (username, password, role, status, member_id) VALUES
('john', 'john123', 'user', 'active', 1),
('jane', 'jane123', 'user', 'active', 2),
('alex', 'alex123', 'user', 'active', 3),
('user', 'user123', 'user', 'active', 5);

-- ============================================================================
-- INSERT SAMPLE ISSUED BOOKS (4 Active Issues)
-- Simulating realistic library usage
-- ============================================================================
INSERT INTO issued_books (book_id, member_id, issue_date, return_date, status, notes) VALUES
-- Recently issued (within last 7 days)
(1, 1, DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 5 DAY), INTERVAL 14 DAY), 'issued', 'First-time borrower'),
(5, 2, DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 3 DAY), INTERVAL 14 DAY), 'issued', 'Regular member'),

-- About to be overdue (issued 12-13 days ago)
(2, 3, DATE_SUB(CURDATE(), INTERVAL 12 DAY), DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 12 DAY), INTERVAL 14 DAY), 'issued', 'Due soon'),
(3, 1, DATE_SUB(CURDATE(), INTERVAL 13 DAY), DATE_ADD(DATE_SUB(CURDATE(), INTERVAL 13 DAY), INTERVAL 14 DAY), 'issued', 'Second book for John');

-- Update book quantities (reduce by 1 for each issued book)
UPDATE books SET quantity = quantity - 1 WHERE id IN (1, 2, 3, 5);

-- ============================================================================
-- INSERT SAMPLE RETURNED BOOKS (5 Past Returns with varying scenarios)
-- ============================================================================
INSERT INTO returned_books (book_id, member_id, issue_date, return_date, fine) VALUES
-- On-time returns (no fine)
(6, 2, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_SUB(CURDATE(), INTERVAL 18 DAY), 0.00),
(7, 4, DATE_SUB(CURDATE(), INTERVAL 25 DAY), DATE_SUB(CURDATE(), INTERVAL 13 DAY), 0.00),

-- Late returns with fines (more than 14 days)
(4, 3, DATE_SUB(CURDATE(), INTERVAL 40 DAY), DATE_SUB(CURDATE(), INTERVAL 20 DAY), 60.00), -- 6 days late = 6*10 = 60
(8, 1, DATE_SUB(CURDATE(), INTERVAL 35 DAY), DATE_SUB(CURDATE(), INTERVAL 18 DAY), 30.00), -- 3 days late = 3*10 = 30
(9, 4, DATE_SUB(CURDATE(), INTERVAL 28 DAY), DATE_SUB(CURDATE(), INTERVAL 10 DAY), 40.00); -- 4 days late = 4*10 = 40

-- ============================================================================
-- INSERT SAMPLE FINES (3 Fine Records)
-- ============================================================================
INSERT INTO fines (member_id, book_id, fine_amount, fine_date, paid, payment_date, notes) VALUES
-- Paid fines
(3, 4, 60.00, DATE_SUB(CURDATE(), INTERVAL 20 DAY), TRUE, DATE_SUB(CURDATE(), INTERVAL 15 DAY), 'Paid in cash'),
(4, 9, 40.00, DATE_SUB(CURDATE(), INTERVAL 10 DAY), TRUE, DATE_SUB(CURDATE(), INTERVAL 5 DAY), 'Paid online'),

-- Unpaid fine
(1, 8, 30.00, DATE_SUB(CURDATE(), INTERVAL 18 DAY), FALSE, NULL, 'Pending payment');

-- ============================================================================
-- VERIFICATION QUERIES WITH DETAILED STATISTICS
-- ============================================================================
SELECT '========================================' as '';
SELECT 'TEST DATA INSERTION COMPLETE!' as Status;
SELECT '========================================' as '';

SELECT '📚 BOOKS SUMMARY:' as Info;
SELECT 
    COUNT(*) as 'Total Books',
    SUM(quantity) as 'Total Copies',
    SUM(CASE WHEN quantity > 0 THEN 1 ELSE 0 END) as 'Available Books',
    SUM(CASE WHEN quantity = 0 THEN 1 ELSE 0 END) as 'Out of Stock'
FROM books;

SELECT '👥 MEMBERS SUMMARY:' as Info;
SELECT 
    COUNT(*) as 'Total Members',
    SUM(CASE WHEN status = 'active' THEN 1 ELSE 0 END) as 'Active',
    SUM(CASE WHEN status = 'inactive' THEN 1 ELSE 0 END) as 'Inactive'
FROM members;

SELECT '👤 USERS SUMMARY:' as Info;
SELECT 
    username, 
    role, 
    status, 
    CASE 
        WHEN member_id IS NULL THEN '(Admin - No Member Link)' 
        ELSE CONCAT('Linked to Member ', member_id) 
    END as Member_Link
FROM users
ORDER BY role DESC, username;

SELECT '📖 ISSUED BOOKS SUMMARY:' as Info;
SELECT 
    COUNT(*) as 'Total Active Issues',
    COUNT(CASE WHEN DATEDIFF(CURDATE(), issue_date) > 14 THEN 1 END) as 'Overdue Books',
    COUNT(CASE WHEN DATEDIFF(return_date, CURDATE()) <= 2 THEN 1 END) as 'Due Within 2 Days'
FROM issued_books 
WHERE return_date IS NULL OR actual_return_date IS NULL;

SELECT '📥 RETURNED BOOKS SUMMARY:' as Info;
SELECT 
    COUNT(*) as 'Total Returns',
    SUM(fine) as 'Total Fines Charged',
    AVG(fine) as 'Average Fine'
FROM returned_books;

SELECT '💰 FINES SUMMARY:' as Info;
SELECT 
    COUNT(*) as 'Total Fines', 
    SUM(fine_amount) as 'Total Amount (₹)',
    SUM(CASE WHEN paid = TRUE THEN fine_amount ELSE 0 END) as 'Paid (₹)',
    SUM(CASE WHEN paid = FALSE THEN fine_amount ELSE 0 END) as 'Unpaid (₹)',
    COUNT(CASE WHEN paid = FALSE THEN 1 END) as 'Pending Count'
FROM fines;

SELECT '========================================' as '';
SELECT '🔐 LOGIN CREDENTIALS:' as '';
SELECT '========================================' as '';
SELECT 'Account Type' as Type, 'Username' as User, 'Password' as Pass, 'Notes' as Description UNION ALL
SELECT '👨‍💼 Admin', 'admin', 'admin123', 'Full system access' UNION ALL
SELECT '👤 User', 'john', 'john123', 'Member: John Doe (2 books issued)' UNION ALL
SELECT '👤 User', 'jane', 'jane123', 'Member: Jane Smith (1 book issued)' UNION ALL
SELECT '👤 User', 'alex', 'alex123', 'Member: Alex Johnson (1 book issued)' UNION ALL
SELECT '👤 User', 'user', 'user123', 'Member: Test User (no activity)';

SELECT '========================================' as '';
SELECT '✅ Sample Data Ready!' as Status;
SELECT '📊 You can now test all features of the system' as Message;
SELECT '🚀 Start the application: java -jar library-management-system-2.0.0.jar' as Command;
SELECT '========================================' as '';
