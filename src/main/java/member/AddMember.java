package member;

import database.DBConnection;
import utils.ValidationUtil;
import utils.Constants;
import utils.PasswordUtil;
import com.toedter.calendar.JDateChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.util.Date;

public class AddMember extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(AddMember.class);
    
    private JTextField uniqueIdField, firstNameField, lastNameField, emailField, contactField, addressField, usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> genderComboBox;
    private JDateChooser dobChooser;
    private JLabel strengthLabel;
    private JProgressBar passwordStrengthBar;
    private JButton saveButton;

    public AddMember() {
        setTitle("Add Member - Library Management System");
        setSize(700, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createMainPanel();
        createFooter();
        
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 135, 84));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Add New Member");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Register a new library member with full details");
        subtitleLabel.setForeground(new Color(200, 230, 201));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Personal Information Section
        addSectionLabel(formPanel, gbc, row++, "📋 Personal Information");
        
        addFormField(formPanel, gbc, row++, "Unique Member ID *", (uniqueIdField = new JTextField(20)), "e.g., M001");
        addFormField(formPanel, gbc, row++, "First Name *", (firstNameField = new JTextField(20)), "First name");
        addFormField(formPanel, gbc, row++, "Last Name *", (lastNameField = new JTextField(20)), "Last name");
        
        // Gender
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel genderLabel = new JLabel("Gender");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(genderLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(genderComboBox, gbc);
        row++;
        
        // Date of Birth
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(dobLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dobChooser = new JDateChooser();
        formPanel.add(dobChooser, gbc);
        row++;

        // Contact Information Section
        addSectionLabel(formPanel, gbc, row++, "📱 Contact Information");
        
        addFormField(formPanel, gbc, row++, "Email *", (emailField = new JTextField(20)), "e.g., user@example.com");
        addFormField(formPanel, gbc, row++, "Contact Number *", (contactField = new JTextField(20)), "e.g., 9876543210");
        addFormField(formPanel, gbc, row++, "Address", (addressField = new JTextField(20)), "Full address");

        // Account Information Section
        addSectionLabel(formPanel, gbc, row++, "🔐 Account Information");
        
        addFormField(formPanel, gbc, row++, "Username *", (usernameField = new JTextField(20)), "Username for login");
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel passLabel = new JLabel("Password *");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(passLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                updatePasswordStrength();
            }
        });
        formPanel.add(passwordField, gbc);
        row++;
        
        // Password strength indicator
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel strengthLabelComp = new JLabel("Strength");
        strengthLabelComp.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formPanel.add(strengthLabelComp, gbc);
        
        gbc.gridx = 1;
        JPanel strengthPanel = new JPanel(new BorderLayout(10, 0));
        strengthPanel.setOpaque(false);
        
        passwordStrengthBar = new JProgressBar(0, 100);
        passwordStrengthBar.setPreferredSize(new Dimension(150, 20));
        strengthPanel.add(passwordStrengthBar, BorderLayout.CENTER);
        
        strengthLabel = new JLabel("Weak");
        strengthLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        strengthLabel.setForeground(new Color(220, 53, 69));
        strengthPanel.add(strengthLabel, BorderLayout.EAST);
        
        formPanel.add(strengthPanel, gbc);
        row++;

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addSectionLabel(JPanel panel, GridBagConstraints gbc, int row, String text) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 8, 8);
        
        JLabel sectionLabel = new JLabel(text);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sectionLabel.setForeground(new Color(25, 135, 84));
        panel.add(sectionLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field, String hint) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(labelComp, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setToolTipText(hint);
        panel.add(field, gbc);
    }

    private void updatePasswordStrength() {
        String password = new String(passwordField.getPassword());
        String strength = PasswordUtil.getPasswordStrength(password);
        
        int score = 0;
        switch (strength) {
            case "Weak":
                strengthLabel.setForeground(new Color(220, 53, 69));
                score = 33;
                break;
            case "Medium":
                strengthLabel.setForeground(new Color(255, 193, 7));
                score = 66;
                break;
            case "Strong":
                strengthLabel.setForeground(new Color(40, 167, 69));
                score = 100;
                break;
        }
        
        strengthLabel.setText(strength);
        passwordStrengthBar.setValue(score);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel infoLabel = new JLabel("Fields marked with * are required");
        infoLabel.setForeground(new Color(108, 117, 125));
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerPanel.add(infoLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        saveButton = new JButton("💾 Save Member");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(25, 135, 84));
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveMember());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        footerPanel.add(buttonPanel, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void saveMember() {
        String uniqueId = ValidationUtil.sanitizeInput(uniqueIdField.getText().trim());
        String firstName = ValidationUtil.sanitizeInput(firstNameField.getText().trim());
        String lastName = ValidationUtil.sanitizeInput(lastNameField.getText().trim());
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String address = ValidationUtil.sanitizeInput(addressField.getText().trim());
        String username = ValidationUtil.sanitizeInput(usernameField.getText().trim());
        String password = new String(passwordField.getPassword());
        String gender = (String) genderComboBox.getSelectedItem();
        Date dob = dobChooser.getDate();

        // Validation
        if (uniqueId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Unique Member ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last Name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (email.isEmpty() || !ValidationUtil.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (contact.isEmpty() || !ValidationUtil.isValidPhone(contact)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number (10 digits required).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!PasswordUtil.meetsSecurityRequirements(password)) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 8 characters with uppercase, lowercase, digit, and special character.", 
                "Weak Password", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Database insert
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO members (unique_member_id, first_name, last_name, email, contact_number, address, gender, date_of_birth, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'active')")) {
            
            pstmt.setString(1, uniqueId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, contact);
            pstmt.setString(6, address.isEmpty() ? null : address);
            pstmt.setString(7, gender);
            pstmt.setDate(8, dob != null ? new java.sql.Date(dob.getTime()) : null);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                // Now create user account
                String hashedPassword = PasswordUtil.hashPassword(password);
                try (PreparedStatement userStmt = conn.prepareStatement(
                        "INSERT INTO users (username, password, role, status) VALUES (?, ?, 'user', 'active')")) {
                    userStmt.setString(1, username);
                    userStmt.setString(2, hashedPassword);
                    userStmt.executeUpdate();
                    
                    logger.info("Member registered successfully: {} {} with username: {}", firstName, lastName, username);
                    JOptionPane.showMessageDialog(this, "✓ Member registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                logger.warn("Duplicate entry: {}", uniqueId);
                JOptionPane.showMessageDialog(this, "Unique ID or Email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                logger.error("Error adding member", e);
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddMember());
    }
}
