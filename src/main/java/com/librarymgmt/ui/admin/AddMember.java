package com.librarymgmt.ui.admin;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AddMember extends JFrame {
    private JTextField nameField, emailField, contactField, usernameField;
    private JPasswordField passwordField;
    private JButton addButton;

    public AddMember() {
        setTitle("Add Member");
        setSize(400, 350);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 30, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(140, 30, 200, 25);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 70, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(140, 70, 200, 25);
        add(emailField);

        JLabel contactLabel = new JLabel("Contact:");
        contactLabel.setBounds(30, 110, 100, 25);
        add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(140, 110, 200, 25);
        add(contactField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 150, 100, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(140, 150, 200, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 190, 100, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(140, 190, 200, 25);
        add(passwordField);

        addButton = new JButton("Add Member");
        addButton.setBounds(120, 240, 150, 30);
        add(addButton);

        addButton.addActionListener(e -> addMember());

        setVisible(true);
    }

    private void addMember() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String username = usernameField.getText().trim();
        String password = String.valueOf(passwordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except contact are required.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction start

            // Insert into members table
            String memberSql = "INSERT INTO members (name, email, contact) VALUES (?, ?, ?)";
            try (PreparedStatement memberStmt = conn.prepareStatement(memberSql, Statement.RETURN_GENERATED_KEYS)) {
                memberStmt.setString(1, name);
                memberStmt.setString(2, email);
                memberStmt.setString(3, contact);
                memberStmt.executeUpdate();

                ResultSet rs = memberStmt.getGeneratedKeys();
                if (rs.next()) {
                    int memberId = rs.getInt(1);

                    // Insert into users table
                    String userSql = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";
                    try (PreparedStatement userStmt = conn.prepareStatement(userSql)) {
                        userStmt.setString(1, username);
                        userStmt.setString(2, password);
                        userStmt.executeUpdate();
                    }

                    conn.commit();
                    JOptionPane.showMessageDialog(this, "Member and user login created successfully.");
                } else {
                    conn.rollback();
                    JOptionPane.showMessageDialog(this, "Failed to retrieve member ID.");
                }
            } catch (SQLException e) {
                conn.rollback();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddMember();
    }
}

