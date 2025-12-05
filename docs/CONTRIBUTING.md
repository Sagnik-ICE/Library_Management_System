# Contributing to Library Management System

Thank you for your interest in contributing to the Library Management System project!

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Follow the existing code style
- Test your changes thoroughly

## How to Contribute

### 1. Fork the Repository
```bash
git clone https://github.com/yourusername/library-management-system.git
cd library-management-system
```

### 2. Create a Feature Branch
```bash
git checkout -b feature/your-feature-name
```

### 3. Make Your Changes
- Follow the existing code structure
- Add package declarations properly
- Use meaningful variable names
- Add comments for complex logic

### 4. Test Your Changes
```bash
# Compile
javac -d out -cp "lib/mysql-connector-java-8.0.33.jar" src/main/java/com/librarymgmt/**/*.java

# Run
java -cp "out;lib/mysql-connector-java-8.0.33.jar" com.librarymgmt.ui.auth.LoginPage
```

### 5. Commit Your Changes
```bash
git add .
git commit -m "feat: add new feature description"
```

Use commit message format:
- `feat:` for new features
- `fix:` for bug fixes
- `docs:` for documentation
- `refactor:` for code improvements
- `test:` for test additions

### 6. Push to Your Fork
```bash
git push origin feature/your-feature-name
```

### 7. Create a Pull Request
- Go to the original repository
- Click "New Pull Request"
- Describe your changes clearly
- Reference any related issues

## Coding Standards

### Package Structure
```
com.librarymgmt
├── config          // Configuration classes
├── ui
│   ├── admin       // Admin UI components
│   ├── user        // User UI components
│   └── auth        // Authentication
└── utils           // Utility functions
```

### Naming Conventions
- Classes: PascalCase (e.g., `LoginPage`)
- Methods: camelCase (e.g., `performLogin()`)
- Constants: UPPER_SNAKE_CASE (e.g., `MAX_QUANTITY`)
- Variables: camelCase (e.g., `memberIdField`)

### Code Style
- Indentation: 4 spaces
- Line length: Max 100 characters
- Braces: Opening brace on same line
- Comments: JavaDoc style for public methods

### Error Handling
- Always use try-catch for database operations
- Show user-friendly error messages
- Log detailed error information
- Never expose stack traces to users

### Database Access
- Always use PreparedStatement (prevents SQL injection)
- Use try-with-resources for resource management
- Handle SQLException properly
- Validate all user input before database operations

### Example Code Pattern
```java
package com.librarymgmt.ui.admin;

import com.librarymgmt.config.DBConnection;
import javax.swing.*;
import java.sql.*;

/**
 * This class handles XYZ functionality.
 * 
 * @author Your Name
 * @version 1.0
 */
public class YourClass extends JFrame {
    private JTextField inputField;
    
    public YourClass() {
        setTitle("Your Title");
        setSize(400, 300);
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Initialize UI components
    }
    
    /**
     * Performs the main action of this class.
     */
    private void performAction() {
        String input = inputField.getText().trim();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input cannot be empty");
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM table WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, input);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Process result
            } else {
                JOptionPane.showMessageDialog(this, "Record not found");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
```

## Areas for Contribution

### High Priority
- [ ] Connection pooling for better performance
- [ ] Comprehensive logging system
- [ ] Unit and integration tests
- [ ] Email notifications for fines
- [ ] Advanced fine management UI

### Medium Priority
- [ ] Report generation (PDF)
- [ ] Data export functionality (Excel, CSV)
- [ ] Search enhancements
- [ ] Batch operations
- [ ] User preference settings

### Low Priority
- [ ] Theme customization
- [ ] Internationalization (i18n)
- [ ] REST API wrapper
- [ ] Mobile companion app

## Reporting Issues

When reporting bugs:
1. Describe the issue clearly
2. Provide steps to reproduce
3. Include Java version and OS
4. Attach relevant logs if possible
5. Suggest a solution if you have one

## Documentation

When contributing code:
1. Add JavaDoc comments to public methods
2. Update README.md if adding features
3. Update DATABASE.md if changing schema
4. Document API changes

## Pull Request Process

1. Ensure all tests pass
2. Update documentation
3. Add/update JavaDoc comments
4. Follow the existing code style
5. Keep commits atomic and well-documented
6. Rebase before merging

## Review Process

- At least one review required before merge
- All CI checks must pass
- Code must follow style guidelines
- Tests must have good coverage

## Questions?

- Open an issue for discussions
- Check existing issues and PRs first
- Be respectful and patient

## License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to make Library Management System better!
