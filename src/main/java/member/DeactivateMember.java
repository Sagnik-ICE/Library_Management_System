package member;

import database.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeactivateMember extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(DeactivateMember.class);
    
    private JTextField memberIdField;
    private JTextField nameField, emailField, statusField;
    private JTextArea reasonArea;
    private JButton searchButton, deactivateButton;

    public DeactivateMember() {
        setTitle("Deactivate Member - Library Management System");
        setSize(650, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createFormPanel();
        
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 53, 69));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("⛔ Deactivate Member");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Suspend member account temporarily");
        subtitleLabel.setForeground(new Color(255, 200, 200));
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

    private void createFormPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Search Section
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(220, 53, 69), 2), 
                        "🔍 Find Member", 0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(220, 53, 69)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel searchFieldPanel = new JPanel(new BorderLayout(5, 0));
        searchFieldPanel.setOpaque(false);
        
        JLabel idLabel = new JLabel("Unique Member ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setPreferredSize(new Dimension(130, 30));
        
        memberIdField = new JTextField();
        memberIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memberIdField.setToolTipText("Enter unique member ID (e.g., MEM001)");
        memberIdField.addActionListener(e -> searchMember());
        
        searchButton = new JButton("🔍 Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(220, 53, 69));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(e -> searchMember());
        
        searchFieldPanel.add(idLabel, BorderLayout.WEST);
        searchFieldPanel.add(memberIdField, BorderLayout.CENTER);
        searchFieldPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchFieldPanel, BorderLayout.NORTH);
        
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Member Details Section
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(108, 117, 125)), 
                        "👤 Member Details", 0, 0, new Font("Segoe UI", Font.BOLD, 13)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        nameField = createReadOnlyField("Full Name:");
        detailsPanel.add(createFieldPanel("Full Name:", nameField));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        emailField = createReadOnlyField("Email:");
        detailsPanel.add(createFieldPanel("Email:", emailField));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        statusField = createReadOnlyField("Current Status:");
        statusField.setForeground(new Color(25, 135, 84));
        statusField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailsPanel.add(createFieldPanel("Status:", statusField));
        
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Reason Section
        JPanel reasonPanel = new JPanel(new BorderLayout(5, 5));
        reasonPanel.setOpaque(false);
        reasonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(108, 117, 125)), 
                        "📝 Deactivation Reason", 0, 0, new Font("Segoe UI", Font.BOLD, 13)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel reasonLabel = new JLabel("Reason for Deactivation:");
        reasonLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        reasonArea = new JTextArea(4, 40);
        reasonArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reasonArea.setLineWrap(true);
        reasonArea.setWrapStyleWord(true);
        reasonArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JScrollPane reasonScroll = new JScrollPane(reasonArea);
        
        reasonPanel.add(reasonLabel, BorderLayout.NORTH);
        reasonPanel.add(reasonScroll, BorderLayout.CENTER);
        
        mainPanel.add(reasonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        deactivateButton = new JButton("⛔ Deactivate Member");
        deactivateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deactivateButton.setForeground(Color.WHITE);
        deactivateButton.setBackground(new Color(220, 53, 69));
        deactivateButton.setFocusPainted(false);
        deactivateButton.setBorderPainted(false);
        deactivateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deactivateButton.setPreferredSize(new Dimension(180, 35));
        deactivateButton.setEnabled(false);
        deactivateButton.addActionListener(e -> deactivateMember());
        buttonPanel.add(deactivateButton);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private JTextField createReadOnlyField(String name) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setEditable(false);
        field.setBackground(new Color(248, 249, 250));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setPreferredSize(new Dimension(130, 30));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }

    private void searchMember() {
        String memberId = memberIdField.getText().trim();
        
        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a member ID", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM members WHERE unique_member_id = ?")) {
            pstmt.setString(1, memberId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    
                    if ("inactive".equalsIgnoreCase(status)) {
                        JOptionPane.showMessageDialog(this, 
                                "This member is already inactive!", 
                                "Already Inactive", 
                                JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        return;
                    }
                    
                    // Check for issued books
                    try (PreparedStatement checkStmt = conn.prepareStatement(
                            "SELECT COUNT(*) FROM issued_books WHERE member_id = ? AND return_date IS NULL")) {
                        checkStmt.setInt(1, rs.getInt("id"));
                        try (ResultSet checkRs = checkStmt.executeQuery()) {
                            if (checkRs.next() && checkRs.getInt(1) > 0) {
                                JOptionPane.showMessageDialog(this, 
                                        "Cannot deactivate! Member has " + checkRs.getInt(1) + " unreturned book(s).", 
                                        "Books Pending", 
                                        JOptionPane.ERROR_MESSAGE);
                                clearForm();
                                return;
                            }
                        }
                    }
                    
                    nameField.setText(rs.getString("first_name") + " " + rs.getString("last_name"));
                    emailField.setText(rs.getString("email"));
                    statusField.setText(status.toUpperCase());
                    deactivateButton.setEnabled(true);
                    
                    logger.info("Member found: {}", memberId);
                } else {
                    JOptionPane.showMessageDialog(this, "Member not found!", "Not Found", JOptionPane.ERROR_MESSAGE);
                    clearForm();
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching member: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deactivateMember() {
        String reason = reasonArea.getText().trim();
        
        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a reason for deactivation", "Reason Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to deactivate this member?\n\nMember: " + nameField.getText() + "\nReason: " + reason, 
                "Confirm Deactivation", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String memberId = memberIdField.getText().trim();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE members SET status = 'inactive' WHERE unique_member_id = ?")) {
            pstmt.setString(1, memberId);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                logger.info("Member deactivated: {} - Reason: {}", memberId, reason);
                JOptionPane.showMessageDialog(this, 
                        "✅ Member deactivated successfully!\n\nMember: " + nameField.getText(), 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to deactivate member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error("Error deactivating member: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        memberIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        statusField.setText("");
        reasonArea.setText("");
        deactivateButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeactivateMember());
    }
}

