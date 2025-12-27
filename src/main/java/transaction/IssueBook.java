package transaction;

import database.DBConnection;
import utils.ValidationUtil;
import utils.Constants;
import com.toedter.calendar.JDateChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.util.Date;

public class IssueBook extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(IssueBook.class);
    
    private JTextField memberIdField, bookIdField;
    private JLabel memberNameLabel, bookTitleLabel, availableQtyLabel;
    private JDateChooser returnDateChooser;
    private JTextArea notesArea;
    private JButton issueButton;

    public IssueBook() {
        setTitle("Issue Book - Library Management System");
        setSize(700, 600);
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
        headerPanel.setBackground(new Color(255, 193, 7));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Issue Book");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Issue a book to a member");
        subtitleLabel.setForeground(new Color(255, 255, 200));
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Member Selection
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel memberLabel = new JLabel("Member ID *");
        memberLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(memberLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        memberIdField = new JTextField(15);
        memberIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memberIdField.addActionListener(e -> loadMemberDetails());
        formPanel.add(memberIdField, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        JButton searchMemberBtn = new JButton("Search");
        searchMemberBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchMemberBtn.addActionListener(e -> loadMemberDetails());
        formPanel.add(searchMemberBtn, gbc);
        row++;

        // Member Info Display
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Member:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        memberNameLabel = new JLabel("Enter Member ID");
        memberNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        memberNameLabel.setForeground(new Color(108, 117, 125));
        formPanel.add(memberNameLabel, gbc);
        gbc.gridwidth = 1;
        row++;

        // Separator
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        JSeparator sep1 = new JSeparator();
        formPanel.add(sep1, gbc);
        gbc.gridwidth = 1;
        row++;

        // Book Selection
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel bookLabel = new JLabel("Book ID *");
        bookLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formPanel.add(bookLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        bookIdField = new JTextField(15);
        bookIdField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bookIdField.addActionListener(e -> loadBookDetails());
        formPanel.add(bookIdField, gbc);
        
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        JButton searchBookBtn = new JButton("Search");
        searchBookBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchBookBtn.addActionListener(e -> loadBookDetails());
        formPanel.add(searchBookBtn, gbc);
        row++;

        // Book Info Display
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Book:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        bookTitleLabel = new JLabel("Enter Book ID");
        bookTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bookTitleLabel.setForeground(new Color(108, 117, 125));
        formPanel.add(bookTitleLabel, gbc);
        gbc.gridwidth = 1;
        row++;

        // Available Quantity
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Available:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        availableQtyLabel = new JLabel("-");
        availableQtyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        availableQtyLabel.setForeground(new Color(40, 167, 69));
        formPanel.add(availableQtyLabel, gbc);
        gbc.gridwidth = 1;
        row++;

        // Separator
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        JSeparator sep2 = new JSeparator();
        formPanel.add(sep2, gbc);
        gbc.gridwidth = 1;
        row++;

        // Return Date
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel returnDateLabel = new JLabel("📅 Expected Return Date *");
        returnDateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(returnDateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        returnDateChooser = new JDateChooser();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DAY_OF_MONTH, Constants.DEFAULT_LOAN_PERIOD_DAYS);
        returnDateChooser.setDate(cal.getTime());
        formPanel.add(returnDateChooser, gbc);
        gbc.gridwidth = 1;
        row++;

        // Notes
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel notesLabel = new JLabel("Notes");
        notesLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(notesLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane notesScroll = new JScrollPane(notesArea);
        formPanel.add(notesScroll, gbc);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel infoLabel = new JLabel("Default loan period: " + Constants.DEFAULT_LOAN_PERIOD_DAYS + " days");
        infoLabel.setForeground(new Color(108, 117, 125));
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerPanel.add(infoLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        issueButton = new JButton("✓ Issue Book");
        issueButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        issueButton.setForeground(Color.WHITE);
        issueButton.setBackground(new Color(40, 167, 69));
        issueButton.setFocusPainted(false);
        issueButton.setBorderPainted(false);
        issueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        issueButton.addActionListener(e -> issueBook());
        buttonPanel.add(issueButton);

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

    private void loadMemberDetails() {
        String memberId = memberIdField.getText().trim();
        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT id, first_name, last_name, status FROM members WHERE unique_member_id = ?")) {
            pstmt.setString(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    if ("inactive".equalsIgnoreCase(status)) {
                        JOptionPane.showMessageDialog(this, "Member is inactive!", "Error", JOptionPane.ERROR_MESSAGE);
                        memberNameLabel.setText("Inactive Member");
                        return;
                    }
                    String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                    memberNameLabel.setText("✓ " + fullName);
                    memberNameLabel.setForeground(new Color(40, 167, 69));
                } else {
                    JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    memberNameLabel.setText("Not found");
                    memberNameLabel.setForeground(new Color(220, 53, 69));
                }
            }
        } catch (SQLException e) {
            logger.error("Error loading member details", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBookDetails() {
        String bookId = bookIdField.getText().trim();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT id, title, quantity FROM books WHERE unique_book_id = ?")) {
            pstmt.setString(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int quantity = rs.getInt("quantity");
                    if (quantity <= 0) {
                        JOptionPane.showMessageDialog(this, "No copies available!", "Error", JOptionPane.ERROR_MESSAGE);
                        bookTitleLabel.setText("Out of Stock");
                        availableQtyLabel.setText("0");
                        return;
                    }
                    String title = rs.getString("title");
                    bookTitleLabel.setText("✓ " + title);
                    bookTitleLabel.setForeground(new Color(40, 167, 69));
                    availableQtyLabel.setText(String.valueOf(quantity) + " copies");
                    availableQtyLabel.setForeground(new Color(40, 167, 69));
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    bookTitleLabel.setText("Not found");
                    bookTitleLabel.setForeground(new Color(220, 53, 69));
                }
            }
        } catch (SQLException e) {
            logger.error("Error loading book details", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void issueBook() {
        String memberId = memberIdField.getText().trim();
        String bookId = bookIdField.getText().trim();
        Date returnDate = returnDateChooser.getDate();
        String notes = notesArea.getText().trim();

        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Member ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (returnDate == null) {
            JOptionPane.showMessageDialog(this, "Please select return date!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getMemberStmt = conn.prepareStatement("SELECT id FROM members WHERE unique_member_id = ?")) {
            
            getMemberStmt.setString(1, memberId);
            int memberId_int = -1;
            try (ResultSet rs = getMemberStmt.executeQuery()) {
                if (rs.next()) {
                    memberId_int = rs.getInt("id");
                }
            }

            if (memberId_int == -1) {
                JOptionPane.showMessageDialog(this, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (PreparedStatement getBookStmt = conn.prepareStatement("SELECT id FROM books WHERE unique_book_id = ?")) {
                getBookStmt.setString(1, bookId);
                int bookId_int = -1;
                try (ResultSet rs = getBookStmt.executeQuery()) {
                    if (rs.next()) {
                        bookId_int = rs.getInt("id");
                    }
                }

                if (bookId_int == -1) {
                    JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert issue record
                try (PreparedStatement issueStmt = conn.prepareStatement(
                        "INSERT INTO issued_books (book_id, member_id, issue_date, return_date, notes) VALUES (?, ?, CURDATE(), ?, ?)")) {
                    issueStmt.setInt(1, bookId_int);
                    issueStmt.setInt(2, memberId_int);
                    issueStmt.setDate(3, new java.sql.Date(returnDate.getTime()));
                    issueStmt.setString(4, notes.isEmpty() ? null : notes);
                    issueStmt.executeUpdate();

                    // Update quantity
                    try (PreparedStatement updateQty = conn.prepareStatement("UPDATE books SET quantity = quantity - 1 WHERE id = ?")) {
                        updateQty.setInt(1, bookId_int);
                        updateQty.executeUpdate();
                    }

                    logger.info("Book issued: {} to {}", bookId, memberId);
                    JOptionPane.showMessageDialog(this, "✓ Book issued successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            }
        } catch (SQLException e) {
            logger.error("Error issuing book", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new IssueBook());
    }
}
