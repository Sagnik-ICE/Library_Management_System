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
import java.util.ArrayList;
import java.util.List;

public class ReactivateMember extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ReactivateMember.class);
    
    private JComboBox<String> memberCombo;
    private JTextField nameField, emailField, contactField, statusField;
    private JButton reactivateButton;
    private List<MemberData> inactiveMembers = new ArrayList<>();

    public ReactivateMember() {
        setTitle("Reactivate Member - Library Management System");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createFormPanel();
        loadInactiveMembers();
        
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 135, 84));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("✅ Reactivate Member");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Restore member account access");
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

    private void createFormPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        // Selection Section
        JPanel selectionPanel = new JPanel(new BorderLayout(10, 10));
        selectionPanel.setOpaque(false);
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(25, 135, 84), 2), 
                        "🔍 Select Inactive Member", 0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(25, 135, 84)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel comboPanel = new JPanel(new BorderLayout(5, 0));
        comboPanel.setOpaque(false);
        
        JLabel memberLabel = new JLabel("Inactive Members:");
        memberLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        memberLabel.setPreferredSize(new Dimension(130, 30));
        
        memberCombo = new JComboBox<>();
        memberCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memberCombo.setToolTipText("Select a member to reactivate");
        memberCombo.addActionListener(e -> displayMemberDetails());
        
        comboPanel.add(memberLabel, BorderLayout.WEST);
        comboPanel.add(memberCombo, BorderLayout.CENTER);
        selectionPanel.add(comboPanel, BorderLayout.NORTH);
        
        mainPanel.add(selectionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Member Details Section
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setOpaque(false);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(108, 117, 125)), 
                        "👤 Member Information", 0, 0, new Font("Segoe UI", Font.BOLD, 13)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        nameField = createReadOnlyField("Full Name:");
        detailsPanel.add(createFieldPanel("Full Name:", nameField));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        emailField = createReadOnlyField("Email:");
        detailsPanel.add(createFieldPanel("Email:", emailField));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        contactField = createReadOnlyField("Contact:");
        detailsPanel.add(createFieldPanel("Contact:", contactField));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        statusField = createReadOnlyField("Current Status:");
        statusField.setForeground(new Color(220, 53, 69));
        statusField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        detailsPanel.add(createFieldPanel("Status:", statusField));
        
        mainPanel.add(detailsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Info Message
        JPanel infoPanel = new JPanel(new BorderLayout(10, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBackground(new Color(255, 243, 205));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 193, 7)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel infoIcon = new JLabel("ℹ️");
        infoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel infoLabel = new JLabel("<html>Reactivating will restore full access to the member account. They will be able to borrow books again.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(133, 100, 4));
        
        infoPanel.add(infoIcon, BorderLayout.WEST);
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        reactivateButton = new JButton("✅ Reactivate Member");
        reactivateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        reactivateButton.setForeground(Color.WHITE);
        reactivateButton.setBackground(new Color(25, 135, 84));
        reactivateButton.setFocusPainted(false);
        reactivateButton.setBorderPainted(false);
        reactivateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reactivateButton.setPreferredSize(new Dimension(180, 35));
        reactivateButton.setEnabled(false);
        reactivateButton.addActionListener(e -> reactivateMember());
        buttonPanel.add(reactivateButton);
        
        JButton refreshButton = new JButton("Refresh List");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(13, 110, 253));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.setPreferredSize(new Dimension(130, 35));
        refreshButton.addActionListener(e -> loadInactiveMembers());
        buttonPanel.add(refreshButton);
        
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

    private void loadInactiveMembers() {
        inactiveMembers.clear();
        memberCombo.removeAllItems();
        memberCombo.addItem("-- Select Inactive Member --");
        
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "SELECT id, unique_member_id, first_name, last_name, email, contact_number FROM members WHERE status = 'inactive' ORDER BY first_name")) {
                    
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            MemberData member = new MemberData();
                            member.id = rs.getInt("id");
                            member.uniqueId = rs.getString("unique_member_id");
                            member.firstName = rs.getString("first_name");
                            member.lastName = rs.getString("last_name");
                            member.email = rs.getString("email");
                            member.contact = rs.getString("contact_number");
                            
                            inactiveMembers.add(member);
                            String displayText = member.uniqueId + " - " + member.firstName + " " + member.lastName;
                            SwingUtilities.invokeLater(() -> memberCombo.addItem(displayText));
                        }
                    }
                    
                    logger.info("Loaded {} inactive members", inactiveMembers.size());
                } catch (SQLException e) {
                    logger.error("Error loading inactive members: {}", e.getMessage());
                    SwingUtilities.invokeLater(() -> 
                            JOptionPane.showMessageDialog(ReactivateMember.this, 
                                    "Database error: " + e.getMessage(), 
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE));
                }
                return null;
            }
            
            @Override
            protected void done() {
                if (inactiveMembers.isEmpty()) {
                    JOptionPane.showMessageDialog(ReactivateMember.this, 
                            "No inactive members found!", 
                            "No Members", 
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void displayMemberDetails() {
        int selectedIndex = memberCombo.getSelectedIndex();
        if (selectedIndex <= 0 || selectedIndex > inactiveMembers.size()) {
            clearForm();
            return;
        }
        
        MemberData member = inactiveMembers.get(selectedIndex - 1);
        nameField.setText(member.firstName + " " + member.lastName);
        emailField.setText(member.email);
        contactField.setText(member.contact);
        statusField.setText("INACTIVE");
        reactivateButton.setEnabled(true);
    }

    private void reactivateMember() {
        int selectedIndex = memberCombo.getSelectedIndex();
        if (selectedIndex <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a member", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        MemberData member = inactiveMembers.get(selectedIndex - 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to reactivate this member?\n\nMember: " + member.firstName + " " + member.lastName + "\nID: " + member.uniqueId, 
                "Confirm Reactivation", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE members SET status = 'active' WHERE id = ?")) {
            pstmt.setInt(1, member.id);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                logger.info("Member reactivated: {}", member.uniqueId);
                JOptionPane.showMessageDialog(this, 
                        "✅ Member reactivated successfully!\n\nMember: " + member.firstName + " " + member.lastName, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                loadInactiveMembers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to reactivate member", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error("Error reactivating member: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        contactField.setText("");
        statusField.setText("");
        reactivateButton.setEnabled(false);
    }

    private static class MemberData {
        int id;
        String uniqueId;
        String firstName;
        String lastName;
        String email;
        String contact;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReactivateMember());
    }
}

