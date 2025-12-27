# 📚 Library Management System

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue.svg)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

A modern, enterprise-grade Library Management System built with Java Swing and MySQL. Features a professional UI, role-based access control, and comprehensive library operations management.

![Library Management System](Images/Effective%20Java.jpg)

## ✨ Features

### 📖 Book Management
- **CRUD Operations**: Complete book lifecycle management
- **Advanced Search**: Search by ISBN, title, author, or genre
- **Inventory Tracking**: Real-time quantity monitoring
- **Detailed Cataloging**: ISBN, publisher, price, and descriptions

### 👥 Member Management
- **Member Registration**: Comprehensive profile management
- **Status Control**: Active/Inactive member states
- **Contact Management**: Email and phone validation
- **Profile Pictures**: Image upload support

### 🔄 Transaction Management
- **Book Issuance**: Automated validation and due date calculation
- **Returns Processing**: Smart fine calculation (₹10/day overdue)
- **Transaction History**: Complete audit trail
- **Fine Management**: Payment tracking and status updates

### 🔐 Security & Authentication
- **BCrypt Password Hashing**: Industry-standard security
- **Role-Based Access**: Admin and User roles
- **Session Management**: Secure authentication flow
- **Auto-Password Upgrade**: Plain text to BCrypt migration

### 🎨 Modern UI/UX
- **Professional Login**: Split-screen design with gradient branding
- **Responsive Design**: Clean, intuitive interfaces
- **Focus Effects**: Visual feedback on user interactions
- **Show/Hide Password**: Enhanced usability

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 17+ LTS |
| **GUI Framework** | Swing | Built-in |
| **Database** | MySQL | 8.0+ |
| **Connection Pool** | HikariCP | 5.1.0 |
| **Password Hashing** | BCrypt | 0.10.2 |
| **Logging** | SLF4J + Logback | 2.0.9 |
| **Build Tool** | Maven | 3.8+ |

## 📁 Project Structure

```
Library_Management_System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── book/              # Book management
│   │   │   │   ├── AddBook.java
│   │   │   │   ├── ViewBooks.java
│   │   │   │   ├── SearchBook.java
│   │   │   │   ├── UpdateBook.java
│   │   │   │   └── DeleteBook.java
│   │   │   ├── member/            # Member management
│   │   │   │   ├── AddMember.java
│   │   │   │   ├── ViewMembers.java
│   │   │   │   ├── SearchMember.java
│   │   │   │   ├── UpdateMember.java
│   │   │   │   ├── DeactivateMember.java
│   │   │   │   └── ReactivateMember.java
│   │   │   ├── transaction/       # Transaction management
│   │   │   │   ├── IssueBook.java
│   │   │   │   ├── ReturnBook.java
│   │   │   │   ├── ViewIssuedBooks.java
│   │   │   │   ├── ViewReturnedBooks.java
│   │   │   │   └── ViewFines.java
│   │   │   ├── login/             # Authentication
│   │   │   │   ├── LoginPage.java
│   │   │   │   ├── AdminMenu.java
│   │   │   │   └── UserMenu.java
│   │   │   ├── database/          # Database layer
│   │   │   │   └── DBConnection.java
│   │   │   ├── models/            # Data models
│   │   │   │   ├── Book.java
│   │   │   │   └── Member.java
│   │   │   ├── utils/             # Utilities
│   │   │   │   ├── ConfigManager.java
│   │   │   │   ├── Constants.java
│   │   │   │   ├── PasswordUtil.java
│   │   │   │   ├── ValidationUtil.java
│   │   │   │   └── exceptions/
│   │   │   │       ├── DatabaseException.java
│   │   │   │       ├── LibraryException.java
│   │   │   │       └── ValidationException.java
│   │   │   └── com/library/management/  # Enterprise architecture (Future)
│   │   │       ├── domain/        # Domain entities
│   │   │       ├── repository/    # Repository pattern
│   │   │       └── infrastructure/ # Infrastructure layer
│   │   └── resources/
│   │       ├── application.properties  # Configuration
│   │       ├── logback.xml            # Logging config
│   │       └── db/
│   │           ├── library_db.sql     # Database schema
│   │           └── test_db.sql        # Sample data
│   └── test/                      # Unit tests (Future)
├── docs/                          # Documentation
│   ├── INSTALLATION.md
│   ├── DATABASE.md
│   └── CONTRIBUTING.md
├── Images/                        # UI Assets
├── target/                        # Build output
├── logs/                          # Application logs
├── pom.xml                        # Maven configuration
├── start.bat                      # Windows launcher
├── start.sh                       # Linux/Mac launcher
├── DEPLOYMENT.md                  # Deployment guide
├── DEVELOPER_GUIDE.md             # Developer documentation
├── LICENSE                        # MIT License
└── README.md                      # This file
```

## 🚀 Quick Start

### Prerequisites
- **Java 17+** (LTS recommended)
- **MySQL 8.0+**
- **Maven 3.8+** (or use included wrapper)

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Sagnik-ICE/Library_Management_System.git
   cd Library_Management_System
   ```

2. **Configure Database**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   db.url=jdbc:mysql://localhost:3306/library_db
   db.username=root
   db.password=your_password
   ```

3. **Setup Database Schema**
   ```bash
   # Create schema and tables
   mysql -u root -p < src/main/resources/db/library_db.sql
   
   # Load sample data (optional)
   mysql -u root -p < src/main/resources/db/test_db.sql
   ```

4. **Build the Project**
   ```bash
   mvn clean package -DskipTests
   ```

5. **Run the Application**
   
   **Windows:**
   ```bash
   start.bat
   ```
   
   **Linux/Mac:**
   ```bash
   ./start.sh
   ```
   
   **Or manually:**
   ```bash
   java -jar target/library-management-system-2.0.0.jar
   ```

## 🔑 Default Credentials

| Role | Username | Password | Access Level |
|------|----------|----------|--------------|
| **Admin** | `admin` | `admin123` | Full system access |
| **User** | `john` | `john123` | Limited access |
| **User** | `jane` | `jane123` | Limited access |

## 📖 Usage Guide

### Admin Functions
- ✅ Complete book and member management
- ✅ Issue and return books for any member
- ✅ View all transactions and fines
- ✅ Manage member accounts (activate/deactivate)
- ✅ Full system administration

### User Functions
- ✅ View issued books
- ✅ Check fines and payment status
- ✅ View personal transaction history
- ❌ Cannot issue/return books (admin only)
- ❌ Cannot manage other members

## 🗄️ Database Schema

The system uses 6 main tables:

- **books** - Book inventory with ISBN, author, genre, quantity
- **members** - Member profiles with contact info and status
- **users** - Authentication with BCrypt hashed passwords
- **issued_books** - Active book issuances with due dates
- **returned_books** - Historical returns with fine calculations
- **fines** - Fine records with payment tracking

See [DATABASE.md](docs/DATABASE.md) for complete schema documentation.

## 📚 Documentation

- **[Installation Guide](docs/INSTALLATION.md)** - Detailed setup instructions
- **[Database Documentation](docs/DATABASE.md)** - Schema and query reference
- **[Developer Guide](DEVELOPER_GUIDE.md)** - Development guidelines
- **[Deployment Guide](DEPLOYMENT.md)** - Production deployment
- **[Contributing](docs/CONTRIBUTING.md)** - Contribution guidelines

## 🔧 Development

### Building from Source
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package application
mvn package

# Skip tests during package
mvn package -DskipTests
```

### Code Structure
- **MVC Pattern**: Clear separation of concerns
- **Repository Pattern**: Database abstraction (in progress)
- **Exception Handling**: Custom exception hierarchy
- **Logging**: SLF4J with Logback configuration
- **Connection Pooling**: HikariCP for performance

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Check MySQL service is running
mysql -u root -p

# Verify database exists
SHOW DATABASES;

# Check configuration
cat src/main/resources/application.properties
```

### Build Failures
```bash
# Clean Maven cache
mvn clean

# Update dependencies
mvn dependency:purge-local-repository

# Rebuild
mvn package -DskipTests
```

See [INSTALLATION.md](docs/INSTALLATION.md) for more troubleshooting tips.

## 🤝 Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](docs/CONTRIBUTING.md) for guidelines.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📋 Roadmap

- [ ] REST API backend
- [ ] Web-based frontend (React/Angular)
- [ ] Mobile application
- [ ] Barcode scanning integration
- [ ] Email notifications
- [ ] Report generation (PDF)
- [ ] Multi-language support
- [ ] Cloud deployment support

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👤 Author

**Sagnik**
- GitHub: [@Sagnik-ICE](https://github.com/Sagnik-ICE)
- Repository: [Library_Management_System](https://github.com/Sagnik-ICE/Library_Management_System)

## 🙏 Acknowledgments

- Java Swing for GUI framework
- MySQL for reliable database
- HikariCP for connection pooling
- BCrypt for password security
- SLF4J & Logback for logging

---

⭐ If you find this project useful, please consider giving it a star!
