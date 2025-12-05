# Installation & Setup Guide

## Prerequisites

- **Java Development Kit (JDK)**: 8 or higher
- **MySQL Server**: 5.7 or higher
- **Git**: For version control

## Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system
```

## Step 2: MySQL Setup

### Option A: Using Command Line

1. Start MySQL Server:
```bash
mysql -u root -p
```

2. Execute the database setup script:
```bash
mysql -u root -p < src/main/resources/db/library_db.sql
```

3. Verify the database:
```sql
USE library_db;
SHOW TABLES;
```

### Option B: Using MySQL Workbench

1. Open MySQL Workbench
2. File → Open SQL Script
3. Navigate to: `src/main/resources/db/library_db.sql`
4. Execute the script

## Step 3: Configure Database Credentials

Edit the file: `src/main/java/com/librarymgmt/config/DBConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/library_db";
private static final String USER = "root";  // Change if needed
private static final String PASSWORD = "your_password";  // Change to your password
```

## Step 4: Download MySQL JDBC Driver

1. Download from: [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
2. Or download version 8.0.33:
   ```bash
   wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-8.0.33.jar
   ```

3. Place the JAR file in a known location (e.g., `lib/` folder)

## Step 5: Compile the Project

Create a `lib` folder if it doesn't exist:
```bash
mkdir lib
```

Copy MySQL JDBC driver to lib folder, then compile:

**Windows:**
```batch
javac -d out -cp "lib/mysql-connector-java-8.0.33.jar" src/main/java/com/librarymgmt/**/*.java
```

**Linux/Mac:**
```bash
javac -d out -cp "lib/mysql-connector-java-8.0.33.jar" src/main/java/com/librarymgmt/**/*.java
```

## Step 6: Run the Application

### Run the Main Application:

**Windows:**
```batch
java -cp "out;lib/mysql-connector-java-8.0.33.jar" com.librarymgmt.ui.auth.LoginPage
```

**Linux/Mac:**
```bash
java -cp "out:lib/mysql-connector-java-8.0.33.jar" com.librarymgmt.ui.auth.LoginPage
```

## Step 7: Login

Use the default admin credentials:
- **Username**: admin
- **Password**: admin123

## Troubleshooting

### Issue: "No suitable driver found for jdbc:mysql"

**Solution**: Ensure MySQL JDBC driver is in the classpath. Add `-cp` with the driver JAR path.

### Issue: "Connection refused"

**Solution**: 
1. Check if MySQL server is running
2. Verify the URL, username, and password in DBConnection.java
3. Check if port 3306 is open

### Issue: "Access denied for user 'root'@'localhost'"

**Solution**: Update the password in DBConnection.java to match your MySQL root password.

### Issue: "Database 'library_db' doesn't exist"

**Solution**: Run the database setup script: `mysql -u root -p < src/main/resources/db/library_db.sql`

## Recommended Directory Structure for Building

```
library-management-system/
├── lib/
│   └── mysql-connector-java-8.0.33.jar
├── out/                    (created after compilation)
├── src/
├── docs/
├── .gitignore
├── README.md
└── INSTALLATION.md
```

## Using Build Scripts (Optional)

Create `build.bat` (Windows):
```batch
@echo off
echo Compiling Library Management System...
javac -d out -cp "lib/mysql-connector-java-8.0.33.jar" src/main/java/com/librarymgmt/**/*.java
echo Build complete!
echo Run with: java -cp "out;lib/mysql-connector-java-8.0.33.jar" com.librarymgmt.ui.auth.LoginPage
```

Create `build.sh` (Linux/Mac):
```bash
#!/bin/bash
echo "Compiling Library Management System..."
javac -d out -cp "lib/mysql-connector-java-8.0.33.jar" src/main/java/com/librarymgmt/**/*.java
echo "Build complete!"
echo "Run with: java -cp 'out:lib/mysql-connector-java-8.0.33.jar' com.librarymgmt.ui.auth.LoginPage"
```

## Database Credentials

After setup, test the connection:

**Windows:**
```batch
java -cp "out;lib/mysql-connector-java-8.0.33.jar" com.librarymgmt.Main
```

You should see: `✅ Connected to database successfully!`

## Getting Help

- Check README.md for feature documentation
- Review DATABASE.md for schema details
- Check individual class JavaDoc comments for implementation details
