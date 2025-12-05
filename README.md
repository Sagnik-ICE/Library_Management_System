# Library Management System

A comprehensive Java-based Library Management System with GUI built using Swing and MySQL database backend.

## Overview

This application provides a complete solution for managing library operations including book inventory, member management, book issuance/returns, and fine tracking.

## Features

### Admin Features
- **Book Management**
  - Add, view, search, update, and delete books
  - Track book quantity

- **Member Management**
  - Add, view, search, and update member information
  - Deactivate/Reactivate members
  - Manage member status

- **Book Operations**
  - Issue books to members
  - Track issued books
  - Process book returns
  - Calculate and track overdue fines

### User Features
- Search for books
- Issue books
- Return books
- View issued books status

## Technology Stack

- **Language**: Java 8+
- **GUI Framework**: Swing
- **Database**: MySQL
- **JDBC**: MySQL Connector/J

## Project Structure

```
library-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── librarymgmt/
│   │   │           ├── config/           # Database configuration
│   │   │           ├── ui/
│   │   │           │   ├── admin/        # Admin UI components
│   │   │           │   ├── user/         # User UI components
│   │   │           │   └── auth/         # Authentication UI
│   │   │           └── utils/            # Utility classes
│   │   └── resources/
│   │       └── db/                       # Database scripts
│   └── test/                             # Test files
├── docs/                                 # Documentation
├── .gitignore
├── README.md
└── pom.xml (Maven configuration - optional)
```

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- MySQL Server 5.7 or higher
- MySQL JDBC Driver (mysql-connector-java.jar)

## Installation

### 1. Database Setup

```bash
mysql -u root -p < src/main/resources/db/library_db.sql
```

### 2. JDBC Driver Setup

Download MySQL JDBC Driver and add to classpath:
- Download: [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
- Or use: `mysql-connector-java-8.0.33.jar`

### 3. Update Database Credentials

Edit `src/main/java/com/librarymgmt/config/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

## Compilation & Execution

### Compile
```bash
javac -d out -cp "mysql-connector-java.jar" src/main/java/com/librarymgmt/**/*.java
```

### Run
```bash
java -cp "out;mysql-connector-java.jar" com.librarymgmt.Main
```

Or launch the login page directly:
```bash
java -cp "out;mysql-connector-java.jar" com.librarymgmt.ui.auth.LoginPage
```

## Database Schema

### Tables
- **books** - Book inventory
- **members** - Member information
- **users** - User login credentials
- **issued_books** - Book issuance tracking
- **returned_books** - Return history
- **fines** - Overdue fines tracking

## Default Credentials

After running the database setup:
- **Username**: admin
- **Password**: admin123
- **Role**: admin

## Key Business Rules

- **Loan Period**: 14 days
- **Fine Rate**: ₹10 per day after grace period
- **Duplicate Prevention**: Same member cannot issue the same book twice without returning first
- **Deactivation Rules**: Members cannot be deactivated if they have:
  - Unreturned books
  - Pending fines

## Package Structure

- `com.librarymgmt` - Main application entry point
- `com.librarymgmt.config` - Configuration classes
- `com.librarymgmt.ui.admin` - Admin interface components
- `com.librarymgmt.ui.user` - User interface components
- `com.librarymgmt.ui.auth` - Authentication interface
- `com.librarymgmt.utils` - Utility functions (reserved for future use)

## Code Quality

- **Error Handling**: Comprehensive try-catch blocks
- **SQL Injection Prevention**: Prepared statements used throughout
- **Transaction Support**: Critical operations use database transactions
- **Input Validation**: All user inputs validated

## Future Enhancements

- [ ] ORM Framework Integration (Hibernate)
- [ ] Connection Pooling
- [ ] Advanced Logging System
- [ ] Email Notifications
- [ ] Report Generation
- [ ] REST API Backend
- [ ] Web UI

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions, please open an issue on GitHub.

## Author

Library Management System Development Team

## Changelog

### Version 1.0.0 (Current)
- Initial release
- Core functionality implemented
- GUI with Swing framework
- MySQL database integration
