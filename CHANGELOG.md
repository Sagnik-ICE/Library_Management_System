# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2025-12-27

### Added
- 🎨 Professional login page with modern split-screen design
- 🔐 BCrypt password hashing for enhanced security
- 📊 HikariCP connection pooling for better performance
- 📝 Comprehensive logging with SLF4J and Logback
- ⚙️ External configuration via application.properties
- 🏗️ Enterprise architecture foundation (Domain, Repository, Infrastructure layers)
- 📚 Enhanced documentation (README, Developer Guide, Deployment Guide)
- 🚀 Launcher scripts for Windows and Linux/Mac (start.bat, start.sh)
- ✨ Modern UI without emoji icons (clean professional look)
- 🔄 Transaction history tracking (issued and returned books)
- 💰 Enhanced fine management with payment tracking

### Changed
- 🔧 Restructured project to follow Maven standard directory layout
- 📦 Migrated from manual compilation to Maven build system
- 🏢 Reorganized packages for better separation of concerns
- 🎨 Removed emoji icons from all UI components
- 🔐 Auto-upgrade plain text passwords to BCrypt on first login
- 📊 Improved database schema with proper indexes and foreign keys

### Fixed
- 🐛 Login authentication with BCrypt password verification
- 🔧 Database connection pooling resource leaks
- 📝 Password validation and hashing issues
- 🎨 UI rendering issues with emoji icons on some systems

### Removed
- ❌ Redundant markdown documentation files
- ❌ Temporary test and debug utility files
- ❌ Old package structure (com.librarymgmt.*)
- ❌ Issue/Return book options from user panel (admin-only now)

## [1.0.0] - 2024-XX-XX

### Added
- Initial release
- Basic book management (CRUD operations)
- Member management
- Book issuance and return functionality
- Fine calculation system
- Admin and User login system
- MySQL database integration
- Swing-based GUI

---

## Version History

### 2.0.0 - Professional Redesign
Complete architectural overhaul with modern UI, enhanced security, and enterprise-grade patterns.

### 1.0.0 - Initial Release
Basic library management functionality with Swing GUI and MySQL backend.
