package transaction;

import database.DBConnection;
import utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReturnBook extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ReturnBook.class);
    
    private JTextField issueIdField;
    private JTextField bookTitleField, memberNameField, issueDateField, dueDateField;
    private JTextField returnDateField, daysOverdueField, fineAmountField;
    private JButton searchButton, returnButton;
    private int currentIssueId = -1;

    public ReturnBook() {
        setTitle("Return Book - Library Management System");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createFormPanel();
        
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 193, 7));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Return Book");
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Process book returns and calculate fines");
        subtitleLabel.setForeground(new Color(102, 102, 102));
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
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(255, 193, 7), 2), 
                        "Find Issue Record", 0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(153, 115, 0)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JPanel searchFieldPanel = new JPanel(new BorderLayout(5, 0));
        searchFieldPanel.setOpaque(false);
        
        JLabel idLabel = new JLabel("Issue ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        idLabel.setPreferredSize(new Dimension(100, 30));
        
        issueIdField = new JTextField();
        issueIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        issueIdField.setToolTipText("Enter the issue ID");
        issueIdField.addActionListener(e -> searchIssueRecord());
        
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        searchButton.setForeground(new Color(51, 51, 51));
        searchButton.setBackground(new Color(255, 193, 7));
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(e -> searchIssueRecord());
        
        searchFieldPanel.add(idLabel, BorderLayout.WEST);
        searchFieldPanel.add(issueIdField, BorderLayout.CENTER);
        searchFieldPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchFieldPanel, BorderLayout.NORTH);
        
        mainPanel.add(searchPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Issue Details Section
        JPanel issuePanel = new JPanel();
        issuePanel.setLayout(new BoxLayout(issuePanel, BoxLayout.Y_AXIS));
        issuePanel.setOpaque(false);
        issuePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(108, 117, 125)), 
                        "Issue Details", 0, 0, new Font("Segoe UI", Font.BOLD, 13)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        bookTitleField = createReadOnlyField("Book Title:");
        issuePanel.add(createFieldPanel("Book Title:", bookTitleField));
        issuePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        memberNameField = createReadOnlyField("Member Name:");
        issuePanel.add(createFieldPanel("Member Name:", memberNameField));
        issuePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        issueDateField = createReadOnlyField("Issue Date:");
        issuePanel.add(createFieldPanel("Issue Date:", issueDateField));
        issuePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        dueDateField = createReadOnlyField("Due Date:");
        issuePanel.add(createFieldPanel("Due Date:", dueDateField));
        
        mainPanel.add(issuePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Return Details Section
        JPanel returnPanel = new JPanel();
        returnPanel.setLayout(new BoxLayout(returnPanel, BoxLayout.Y_AXIS));
        returnPanel.setOpaque(false);
        returnPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(25, 135, 84), 2), 
                        "Return Information", 0, 0, new Font("Segoe UI", Font.BOLD, 13), new Color(25, 135, 84)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        returnDateField = createReadOnlyField("Return Date:");
        returnDateField.setText(LocalDate.now().toString());
        returnDateField.setBackground(new Color(240, 255, 240));
        returnPanel.add(createFieldPanel("Return Date:", returnDateField));
        returnPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        daysOverdueField = createReadOnlyField("Days Overdue:");
        daysOverdueField.setForeground(new Color(220, 53, 69));
        daysOverdueField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        returnPanel.add(createFieldPanel("Days Overdue:", daysOverdueField));
        returnPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        fineAmountField = createReadOnlyField("Fine Amount:");
        fineAmountField.setForeground(new Color(220, 53, 69));
        fineAmountField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnPanel.add(createFieldPanel("Fine Amount:", fineAmountField));
        
        mainPanel.add(returnPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Fine Info
        JPanel infoPanel = new JPanel(new BorderLayout(10, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBackground(new Color(255, 243, 205));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 193, 7)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel infoIcon = new JLabel("ℹ️");
        infoIcon.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        JLabel infoLabel = new JLabel("<html>Fine rate: ₹" + Constants.DEFAULT_FINE_PER_DAY + " per day. Fine will be automatically calculated and recorded.</html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(133, 100, 4));
        
        infoPanel.add(infoIcon, BorderLayout.WEST);
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        mainPanel.add(infoPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        returnButton = new JButton("Process Return");
        returnButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        returnButton.setForeground(Color.WHITE);
        returnButton.setBackground(new Color(25, 135, 84));
        returnButton.setFocusPainted(false);
        returnButton.setBorderPainted(false);
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        returnButton.setPreferredSize(new Dimension(160, 35));
        returnButton.setEnabled(false);
        returnButton.addActionListener(e -> processReturn());
        buttonPanel.add(returnButton);
        
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
        label.setPreferredSize(new Dimension(120, 30));
        
        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }

    private void searchIssueRecord() {
        String issueIdStr = issueIdField.getText().trim();
        
        if (issueIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an issue ID", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int issueId = Integer.parseInt(issueIdStr);
            
            String query = "SELECT ib.id, ib.issue_date, ib.due_date, ib.book_id, " +
                    "b.title AS book_title, " +
                    "CONCAT(m.first_name, ' ', m.last_name) AS member_name " +
                    "FROM issued_books ib " +
                    "JOIN books b ON ib.book_id = b.id " +
                    "JOIN members m ON ib.member_id = m.id " +
                    "WHERE ib.id = ? AND ib.return_date IS NULL";
            
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, issueId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        currentIssueId = rs.getInt("id");
                        bookTitleField.setText(rs.getString("book_title"));
                        memberNameField.setText(rs.getString("member_name"));
                        issueDateField.setText(rs.getDate("issue_date").toString());
                        dueDateField.setText(rs.getDate("due_date").toString());
                        
                        LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                        LocalDate returnDate = LocalDate.now();
                        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
                        
                        if (daysOverdue > 0) {
                            daysOverdueField.setText(String.valueOf(daysOverdue));
                            double fine = daysOverdue * Constants.DEFAULT_FINE_PER_DAY;
                            fineAmountField.setText("₹" + String.format("%.2f", fine));
                        } else {
                            daysOverdueField.setText("0 (On Time)");
                            fineAmountField.setText("₹0.00");
                            daysOverdueField.setForeground(new Color(25, 135, 84));
                            fineAmountField.setForeground(new Color(25, 135, 84));
                        }
                        
                        returnButton.setEnabled(true);
                        logger.info("Issue record found: ID={}", issueId);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                                "Issue record not found or already returned!", 
                                "Not Found", 
                                JOptionPane.ERROR_MESSAGE);
                        clearForm();
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid issue ID format", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            logger.error("Error searching issue record: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processReturn() {
        if (currentIssueId == -1) {
            JOptionPane.showMessageDialog(this, "No issue record selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Confirm book return?\n\nBook: " + bookTitleField.getText() + 
                "\nMember: " + memberNameField.getText() + 
                "\nFine: " + fineAmountField.getText(), 
                "Confirm Return", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Update issued_books
            String updateIssue = "UPDATE issued_books SET return_date = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateIssue)) {
                pstmt.setDate(1, Date.valueOf(LocalDate.now()));
                pstmt.setInt(2, currentIssueId);
                pstmt.executeUpdate();
            }
            
            // Get book_id and member_id
            int bookId = -1, memberId = -1;
            String getIds = "SELECT book_id, member_id FROM issued_books WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(getIds)) {
                pstmt.setInt(1, currentIssueId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        bookId = rs.getInt("book_id");
                        memberId = rs.getInt("member_id");
                    }
                }
            }
            
            // Insert into returned_books
            String insertReturn = "INSERT INTO returned_books (issue_id, book_id, member_id, return_date, fine_amount) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertReturn)) {
                pstmt.setInt(1, currentIssueId);
                pstmt.setInt(2, bookId);
                pstmt.setInt(3, memberId);
                pstmt.setDate(4, Date.valueOf(LocalDate.now()));
                
                String fineStr = fineAmountField.getText().replace("₹", "").trim();
                double fine = Double.parseDouble(fineStr);
                pstmt.setDouble(5, fine);
                
                pstmt.executeUpdate();
            }
            
            // Update book quantity
            String updateBook = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateBook)) {
                pstmt.setInt(1, bookId);
                pstmt.executeUpdate();
            }
            
            // If fine > 0, insert into fines table
            String fineStr = fineAmountField.getText().replace("₹", "").trim();
            double fine = Double.parseDouble(fineStr);
            if (fine > 0) {
                String insertFine = "INSERT INTO fines (member_id, book_id, fine_amount, fine_reason, fine_status) VALUES (?, ?, ?, ?, 'unpaid')";
                try (PreparedStatement pstmt = conn.prepareStatement(insertFine)) {
                    pstmt.setInt(1, memberId);
                    pstmt.setInt(2, bookId);
                    pstmt.setDouble(3, fine);
                    pstmt.setString(4, "Late return - " + daysOverdueField.getText() + " days overdue");
                    pstmt.executeUpdate();
                }
            }
            
            conn.commit();
            logger.info("Book returned successfully: IssueID={}, Fine=₹{}", currentIssueId, fine);
            
            JOptionPane.showMessageDialog(this, 
                    "✅ Book returned successfully!\n\nFine Amount: " + fineAmountField.getText(), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.error("Error rolling back: {}", ex.getMessage());
                }
            }
            logger.error("Error processing return: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection: {}", e.getMessage());
                }
            }
        }
    }

    private void clearForm() {
        currentIssueId = -1;
        issueIdField.setText("");
        bookTitleField.setText("");
        memberNameField.setText("");
        issueDateField.setText("");
        dueDateField.setText("");
        returnDateField.setText(LocalDate.now().toString());
        daysOverdueField.setText("");
        daysOverdueField.setForeground(new Color(220, 53, 69));
        fineAmountField.setText("");
        fineAmountField.setForeground(new Color(220, 53, 69));
        returnButton.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReturnBook());
    }
}

