# 📚 Developer Documentation

## Table of Contents
- [Architecture Overview](#architecture-overview)
- [Package Structure](#package-structure)
- [Core Components](#core-components)
- [Database Access](#database-access)
- [Utilities](#utilities)
- [Security](#security)
- [Logging](#logging)
- [Best Practices](#best-practices)

---

## Architecture Overview

### High-Level Architecture

```
┌─────────────────────────────────────────────────┐
│              Presentation Layer                  │
│  (Swing GUI - JFrame, JPanel, JTable, etc.)     │
└──────────────┬──────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────┐
│             Business Logic Layer                 │
│    (book/, member/, transaction/, login/)       │
└──────────────┬──────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────┐
│             Data Access Layer                    │
│      (database/ - JDBC + HikariCP)              │
└──────────────┬──────────────────────────────────┘
               │
┌──────────────▼──────────────────────────────────┐
│             Database Layer                       │
│          (MySQL 8.0+)                           │
└─────────────────────────────────────────────────┘
```

### Design Patterns

1. **Singleton Pattern** - ConfigManager, DBConnection
2. **MVC Pattern** - Separation of UI, logic, and data
3. **Factory Pattern** - UI component creation
4. **DAO Pattern** - Database access objects

---

## Package Structure

### Core Packages

```
src/
├── book/                   # Book management
│   ├── AddBook.java
│   ├── UpdateBook.java
│   ├── ViewBooks.java
│   ├── SearchBook.java
│   └── DeleteBook.java
│
├── member/                 # Member management
│   ├── AddMember.java
│   ├── UpdateMember.java
│   ├── ViewMembers.java
│   ├── SearchMember.java
│   ├── DeactivateMember.java
│   └── ReactivateMember.java
│
├── transaction/            # Transactions
│   ├── IssueBook.java
│   ├── ReturnBook.java
│   ├── ViewIssuedBooks.java
│   ├── ViewReturnedBooks.java
│   └── ViewFines.java
│
├── login/                  # Authentication & Navigation
│   ├── LoginPage.java
│   ├── AdminMenu.java
│   └── UserMenu.java
│
├── database/               # Database connectivity
│   └── DBConnection.java
│
├── utils/                  # Utilities
│   ├── ConfigManager.java
│   ├── PasswordUtil.java
│   ├── ValidationUtil.java
│   ├── Constants.java
│   └── exceptions/
│       ├── LibraryException.java
│       ├── DatabaseException.java
│       └── ValidationException.java
│
└── models/                 # Entity models
    ├── Book.java
    └── Member.java
```

---

## Core Components

### ConfigManager

**Purpose**: Centralized configuration management

```java
// Usage example
ConfigManager config = ConfigManager.getInstance();
String dbUrl = config.getDatabaseUrl();
int poolSize = config.getHikariMaxPoolSize();
double fineRate = config.getFineRatePerDay();
```

**Key Methods**:
- `getInstance()` - Get singleton instance
- `getProperty(String key)` - Get string property
- `getIntProperty(String key, int defaultValue)` - Get integer
- `getDoubleProperty(String key, double defaultValue)` - Get double
- `getDatabaseUrl()` - Get DB URL
- `getDatabaseUsername()` - Get DB username
- `getDatabasePassword()` - Get DB password

### DBConnection

**Purpose**: Database connection pool management using HikariCP

```java
// Usage example
try (Connection conn = DBConnection.getConnection()) {
    // Use connection
    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books");
    ResultSet rs = pstmt.executeQuery();
    // Process results
} catch (SQLException e) {
    logger.error("Database error", e);
}
```

**Key Methods**:
- `getConnection()` - Get connection from pool
- `closeConnection(Connection conn)` - Return connection to pool
- `shutdown()` - Shutdown datasource
- `testConnection()` - Test database connectivity
- `getPoolStats()` - Get connection pool statistics

**Connection Pool Configuration**:
- Max Pool Size: 10 (configurable)
- Min Idle: 5 (configurable)
- Connection Timeout: 30 seconds
- Idle Timeout: 10 minutes
- Max Lifetime: 30 minutes

### PasswordUtil

**Purpose**: Secure password handling with BCrypt

```java
// Hashing password
String hashedPassword = PasswordUtil.hashPassword("plainPassword");

// Verifying password
boolean isValid = PasswordUtil.verifyPassword("plainPassword", hashedPassword);

// Check password strength
String strength = PasswordUtil.getPasswordStrength("MyP@ssw0rd");
// Returns: "Weak", "Medium", or "Strong"

// Migrate old plain-text password
String newHash = PasswordUtil.migratePassword("oldPassword");
```

**Security Features**:
- BCrypt algorithm with 12 rounds (configurable)
- Salt automatically generated
- Password strength validation
- Migration support for legacy passwords

### ValidationUtil

**Purpose**: Comprehensive input validation

```java
// Email validation
boolean isValid = ValidationUtil.isValidEmail("user@example.com");

// Phone validation
boolean isValid = ValidationUtil.isValidPhone("1234567890");

// Required field validation with error message
String error = ValidationUtil.validateRequired(value, "Field Name");
if (error != null) {
    // Show error to user
}

// Custom validation
if (!ValidationUtil.isValidName(name)) {
    JOptionPane.showMessageDialog(this, "Invalid name format");
}
```

**Validation Methods**:
- `isNotEmpty(String value)` - Check non-empty
- `isValidEmail(String email)` - Email format
- `isValidPhone(String phone)` - Phone number (10-15 digits)
- `isValidISBN(String isbn)` - ISBN format
- `isValidPassword(String password)` - Password strength
- `isValidUsername(String username)` - Username format
- `isValidName(String name)` - Name format (alphabets only)
- `isValidNumber(String value)` - Numeric validation
- `isValidPrice(String price)` - Positive decimal
- `sanitizeInput(String input)` - SQL injection prevention

---

## Database Access

### Connection Pattern

**Standard Pattern**:
```java
Connection conn = null;
PreparedStatement pstmt = null;
ResultSet rs = null;

try {
    conn = DBConnection.getConnection();
    String sql = "SELECT * FROM books WHERE id = ?";
    pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, bookId);
    rs = pstmt.executeQuery();
    
    while (rs.next()) {
        // Process results
        String title = rs.getString("title");
        String author = rs.getString("author");
    }
} catch (SQLException e) {
    logger.error("Database error", e);
    throw new DatabaseException("Failed to fetch book", e);
} finally {
    if (rs != null) try { rs.close(); } catch (SQLException e) {}
    if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
    if (conn != null) DBConnection.closeConnection(conn);
}
```

**Modern Try-With-Resources Pattern**:
```java
try (Connection conn = DBConnection.getConnection();
     PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE id = ?")) {
    
    pstmt.setInt(1, bookId);
    
    try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            // Process results
        }
    }
} catch (SQLException e) {
    logger.error("Database error", e);
    throw new DatabaseException("Failed to fetch book", e);
}
```

### Transaction Pattern

```java
Connection conn = null;
try {
    conn = DBConnection.getConnection();
    conn.setAutoCommit(false); // Start transaction
    
    // Multiple operations
    PreparedStatement pstmt1 = conn.prepareStatement("UPDATE books SET quantity = quantity - 1 WHERE id = ?");
    pstmt1.setInt(1, bookId);
    pstmt1.executeUpdate();
    
    PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO issued_books (book_id, member_id, issue_date) VALUES (?, ?, ?)");
    pstmt2.setInt(1, bookId);
    pstmt2.setInt(2, memberId);
    pstmt2.setDate(3, new java.sql.Date(System.currentTimeMillis()));
    pstmt2.executeUpdate();
    
    conn.commit(); // Commit transaction
    logger.info("Transaction completed successfully");
    
} catch (SQLException e) {
    if (conn != null) {
        try {
            conn.rollback(); // Rollback on error
            logger.warn("Transaction rolled back");
        } catch (SQLException ex) {
            logger.error("Rollback failed", ex);
        }
    }
    throw new DatabaseException("Transaction failed", e);
} finally {
    if (conn != null) {
        try {
            conn.setAutoCommit(true);
            DBConnection.closeConnection(conn);
        } catch (SQLException e) {
            logger.error("Error resetting auto-commit", e);
        }
    }
}
```

---

## Utilities

### Constants Usage

```java
import utils.Constants;

// UI Colors
panel.setBackground(Constants.HEADER_BLUE);

// UI Dimensions
setSize(Constants.DEFAULT_WINDOW_WIDTH, Constants.DEFAULT_WINDOW_HEIGHT);

// Fonts
label.setFont(Constants.HEADER_FONT);

// Business Rules
double fine = daysOverdue * Constants.DEFAULT_FINE_PER_DAY;

// Status
if (member.getStatus().equals(Constants.STATUS_ACTIVE)) {
    // Member is active
}

// Messages
JOptionPane.showMessageDialog(this, Constants.MSG_SUCCESS, 
    Constants.TITLE_SUCCESS, JOptionPane.INFORMATION_MESSAGE);
```

### Exception Handling

```java
import utils.exceptions.*;

// Throwing custom exceptions
if (!ValidationUtil.isValidEmail(email)) {
    throw new ValidationException("Invalid email format: " + email);
}

try {
    // Database operation
} catch (SQLException e) {
    throw new DatabaseException("Failed to save book", e);
}

// Catching in UI
try {
    // Business logic
} catch (ValidationException e) {
    logger.warn("Validation failed", e);
    JOptionPane.showMessageDialog(this, e.getMessage(), 
        Constants.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
} catch (DatabaseException e) {
    logger.error("Database error", e);
    JOptionPane.showMessageDialog(this, "Database error occurred. Please try again.", 
        Constants.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
} catch (LibraryException e) {
    logger.error("Application error", e);
    JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), 
        Constants.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
}
```

---

## Security

### Password Security

**Hashing on Registration**:
```java
String plainPassword = passwordField.getText();
String hashedPassword = PasswordUtil.hashPassword(plainPassword);

// Save to database
String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
pstmt.setString(1, username);
pstmt.setString(2, hashedPassword); // Store hashed password
pstmt.setString(3, role);
```

**Verification on Login**:
```java
String inputPassword = passwordField.getText();
String storedHash = rs.getString("password");

if (PasswordUtil.verifyPassword(inputPassword, storedHash)) {
    // Login successful
} else {
    // Login failed
}
```

### SQL Injection Prevention

**Always use PreparedStatements**:
```java
// GOOD - Parameterized query
String sql = "SELECT * FROM books WHERE title = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, userInput);

// BAD - String concatenation (vulnerable to SQL injection)
String sql = "SELECT * FROM books WHERE title = '" + userInput + "'"; // DON'T DO THIS!
```

### Input Sanitization

```java
String userInput = inputField.getText();

// Validate
if (!ValidationUtil.isValidName(userInput)) {
    throw new ValidationException("Invalid input format");
}

// Sanitize
String sanitized = ValidationUtil.sanitizeInput(userInput);

// Use in query
pstmt.setString(1, sanitized);
```

---

## Logging

### SLF4J Usage

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    
    public void addBook(Book book) {
        logger.debug("Adding book: {}", book.getTitle());
        
        try {
            // Database operation
            logger.info("Book added successfully: ID={}", book.getId());
        } catch (SQLException e) {
            logger.error("Failed to add book: {}", book.getTitle(), e);
            throw new DatabaseException("Book creation failed", e);
        }
    }
}
```

### Log Levels

- **ERROR**: Critical errors that need immediate attention
- **WARN**: Warning messages for potential issues
- **INFO**: General informational messages
- **DEBUG**: Detailed debug information (disabled in production)
- **TRACE**: Very detailed tracing information

### Log Configuration

Edit `logback.xml` to adjust logging:

```xml
<!-- Package-specific logging -->
<logger name="database" level="DEBUG"/>
<logger name="login" level="INFO"/>
<logger name="book" level="WARN"/>
```

---

## Best Practices

### Code Standards

1. **Always close resources**
   ```java
   try (Connection conn = DBConnection.getConnection()) {
       // Use connection
   } // Automatically closed
   ```

2. **Use constants instead of magic numbers**
   ```java
   // Good
   if (quantity > Constants.MIN_QUANTITY) {}
   
   // Bad
   if (quantity > 0) {}
   ```

3. **Validate all user inputs**
   ```java
   String email = emailField.getText();
   if (!ValidationUtil.isValidEmail(email)) {
       showError("Invalid email format");
       return;
   }
   ```

4. **Log important operations**
   ```java
   logger.info("User {} logged in successfully", username);
   logger.error("Failed to process transaction", exception);
   ```

5. **Handle exceptions appropriately**
   ```java
   try {
       // Operation
   } catch (SQLException e) {
       logger.error("Database error", e);
       throw new DatabaseException("Operation failed", e);
   }
   ```

6. **Use transactions for multiple operations**
   ```java
   conn.setAutoCommit(false);
   try {
       // Multiple operations
       conn.commit();
   } catch (SQLException e) {
       conn.rollback();
   }
   ```

7. **Separate concerns**
   - UI logic in Swing classes
   - Business logic in service classes
   - Data access in DAO classes

### Performance Tips

1. **Use connection pooling** (already implemented with HikariCP)
2. **Close ResultSets promptly**
3. **Use batch operations for multiple inserts**
4. **Index frequently queried columns**
5. **Limit result sets with LIMIT clause**

### Security Checklist

- [ ] All passwords hashed with BCrypt
- [ ] All SQL queries use PreparedStatements
- [ ] All user inputs validated
- [ ] Database credentials externalized
- [ ] Proper error messages (don't expose system details)
- [ ] Session timeout configured
- [ ] Regular security audits

---

## Testing

### Manual Testing Checklist

- [ ] Login with valid credentials
- [ ] Login with invalid credentials
- [ ] Add book with all fields
- [ ] Add book with missing optional fields
- [ ] Update book information
- [ ] Delete book (check constraints)
- [ ] Issue book to active member
- [ ] Issue book to inactive member (should fail)
- [ ] Return book on time (no fine)
- [ ] Return book late (calculate fine)
- [ ] Search with various criteria
- [ ] View paginated lists
- [ ] Connection pool under load

---

## Troubleshooting

### Common Issues

1. **Connection Pool Exhausted**
   - Check for unclosed connections
   - Increase pool size in config
   - Review slow queries

2. **Password Mismatch**
   - Ensure BCrypt is used consistently
   - Check password requirements

3. **SQL Exceptions**
   - Check database connectivity
   - Verify table schema
   - Review SQL syntax

4. **UI Freezing**
   - Long operations should use SwingWorker
   - Don't block EDT thread

---

## Contributing

When adding new features:

1. Follow existing code patterns
2. Add logging for important operations
3. Validate all inputs
4. Use PreparedStatements for SQL
5. Handle exceptions properly
6. Update documentation
7. Test thoroughly

---

**Version**: 2.0.0  
**Last Updated**: December 2025
