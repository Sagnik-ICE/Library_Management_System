package member;

import database.DBConnection;
import utils.ValidationUtil;
import utils.PasswordUtil;
import utils.Constants;
import com.toedter.calendar.JDateChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class UpdateMember extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(UpdateMember.class);
    
    private JTextField searchField, uniqueIdField, firstNameField, lastNameField, emailField, contactField;
    private JComboBox<String> genderComboBox, statusComboBox;
    private JTextArea addressArea;
    private JDateChooser dobChooser;
    private JPasswordField passwordField;
    private JButton searchButton, updateButton;
    private int currentMemberId = -1;

    public UpdateMember() {
        setTitle("Update Member - Library Management System");
        setSize(700, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createSearchPanel();
        createFormPanel();
        createFooter();
        
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 135, 84));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("✏️ Update Member");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Search and update member information");
        subtitleLabel.setForeground(new Color(200, 255, 220));
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

    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        searchPanel.setBackground(new Color(248, 249, 250));
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));
        
        JLabel searchLabel = new JLabel("🔍 Enter Member ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.addActionListener(e -> searchMember());
        searchPanel.add(searchField);
        
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(25, 135, 84));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> searchMember());
        searchPanel.add(searchButton);
        
        JPanel combinedPanel = new JPanel(new BorderLayout());
        JPanel header = (JPanel) getContentPane().getComponent(0);
        combinedPanel.add(header, BorderLayout.NORTH);
        combinedPanel.add(searchPanel, BorderLayout.SOUTH);
        add(combinedPanel, BorderLayout.NORTH);
    }

    private void createFormPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        addFormField(formPanel, gbc, row++, "Unique Member ID", (uniqueIdField = new JTextField(20)), "Unique identifier");
        uniqueIdField.setEnabled(false);
        
        addFormField(formPanel, gbc, row++, "First Name *", (firstNameField = new JTextField(20)), "Member's first name");
        addFormField(formPanel, gbc, row++, "Last Name *", (lastNameField = new JTextField(20)), "Member's last name");
        addFormField(formPanel, gbc, row++, "Email *", (emailField = new JTextField(20)), "Email address");
        addFormField(formPanel, gbc, row++, "Contact *", (contactField = new JTextField(20)), "10-digit phone number");
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel genderLabel = new JLabel("Gender *");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(genderLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(genderComboBox, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(dobLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dobChooser = new JDateChooser();
        dobChooser.setDateFormatString("yyyy-MM-dd");
        formPanel.add(dobChooser, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(addressLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane addressScroll = new JScrollPane(addressArea);
        formPanel.add(addressScroll, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel statusLabel = new JLabel("Status *");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] statuses = {"active", "inactive"};
        statusComboBox = new JComboBox<>(statuses);
        statusComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(statusComboBox, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel passwordLabel = new JLabel("New Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        passwordField.setToolTipText("Leave empty to keep current password");
        formPanel.add(passwordField, gbc);

        setFormEnabled(false);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field, String hint) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 0;
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(labelComp, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setToolTipText(hint);
        panel.add(field, gbc);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        updateButton = new JButton("💾 Update Member");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setFocusPainted(false);
        updateButton.setBorderPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateMember());
        buttonPanel.add(updateButton);

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

    private void searchMember() {
        String searchId = searchField.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM members WHERE unique_member_id = ? OR id = ?")) {
            
            try {
                int id = Integer.parseInt(searchId);
                pstmt.setString(1, searchId);
                pstmt.setInt(2, id);
            } catch (NumberFormatException e) {
                pstmt.setString(1, searchId);
                pstmt.setInt(2, -1);
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    currentMemberId = rs.getInt("id");
                    uniqueIdField.setText(rs.getString("unique_member_id"));
                    firstNameField.setText(rs.getString("first_name"));
                    lastNameField.setText(rs.getString("last_name"));
                    emailField.setText(rs.getString("email"));
                    contactField.setText(rs.getString("contact_number"));
                    genderComboBox.setSelectedItem(rs.getString("gender"));
                    addressArea.setText(rs.getString("address"));
                    statusComboBox.setSelectedItem(rs.getString("status"));
                    
                    Date dob = rs.getDate("date_of_birth");
                    if (dob != null) {
                        dobChooser.setDate(new java.util.Date(dob.getTime()));
                    }
                    
                    setFormEnabled(true);
                    updateButton.setEnabled(true);
                    
                    logger.info("Member loaded for update: {}", uniqueIdField.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "Member not found!", "Not Found", JOptionPane.ERROR_MESSAGE);
                    clearForm();
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching member", e);
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMember() {
        if (currentMemberId == -1) {
            JOptionPane.showMessageDialog(this, "Please search for a member first", "No Member Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String contact = contactField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String address = addressArea.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();
        String password = new String(passwordField.getPassword()).trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All required fields must be filled", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidationUtil.isValidPhone(contact)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number (must be 10 digits)", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // Update member
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE members SET first_name=?, last_name=?, email=?, contact_number=?, gender=?, address=?, date_of_birth=?, status=? WHERE id=?")) {
                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
                    pstmt.setString(3, email);
                    pstmt.setString(4, contact);
                    pstmt.setString(5, gender);
                    pstmt.setString(6, address.isEmpty() ? null : address);
                    pstmt.setDate(7, dobChooser.getDate() != null ? new java.sql.Date(dobChooser.getDate().getTime()) : null);
                    pstmt.setString(8, status);
                    pstmt.setInt(9, currentMemberId);
                    pstmt.executeUpdate();
                }
                
                // Update password if provided
                if (!password.isEmpty()) {
                    String hashedPassword = PasswordUtil.hashPassword(password);
                    try (PreparedStatement pstmt = conn.prepareStatement(
                            "UPDATE users SET password=? WHERE username=(SELECT email FROM members WHERE id=?)")) {
                        pstmt.setString(1, hashedPassword);
                        pstmt.setInt(2, currentMemberId);
                        pstmt.executeUpdate();
                    }
                }
                
                conn.commit();
                logger.info("Member updated successfully: {}", firstName + " " + lastName);
                JOptionPane.showMessageDialog(this, "✓ Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            logger.error("Error updating member", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFormEnabled(boolean enabled) {
        firstNameField.setEnabled(enabled);
        lastNameField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        contactField.setEnabled(enabled);
        genderComboBox.setEnabled(enabled);
        addressArea.setEnabled(enabled);
        dobChooser.setEnabled(enabled);
        statusComboBox.setEnabled(enabled);
        passwordField.setEnabled(enabled);
    }

    private void clearForm() {
        currentMemberId = -1;
        uniqueIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        contactField.setText("");
        genderComboBox.setSelectedIndex(0);
        addressArea.setText("");
        dobChooser.setDate(null);
        statusComboBox.setSelectedIndex(0);
        passwordField.setText("");
        setFormEnabled(false);
        updateButton.setEnabled(false);
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateMember());
    }
}

