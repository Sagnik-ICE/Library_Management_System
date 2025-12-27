package book;

import database.DBConnection;
import utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DeleteBook extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(DeleteBook.class);
    
    private JTextField searchField;
    private JTextArea bookInfoArea;
    private JButton searchButton, deleteButton;
    private int currentBookId = -1;

    public DeleteBook() {
        setTitle("Delete Book - Library Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createSearchPanel();
        createInfoPanel();
        createFooter();
        
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 53, 69)); // Red header for delete
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("🗑️ Delete Book");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Search and permanently remove book from library");
        subtitleLabel.setForeground(new Color(255, 220, 220));
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
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        searchPanel.setBackground(new Color(248, 249, 250));
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));
        
        JLabel searchLabel = new JLabel("🔍 Enter Book ID or Unique ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.addActionListener(e -> searchBook());
        searchPanel.add(searchField);
        
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(Constants.HEADER_BLUE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> searchBook());
        searchPanel.add(searchButton);
        
        add(searchPanel, BorderLayout.CENTER);
    }

    private void createInfoPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel infoLabel = new JLabel("Book Information");
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(infoLabel, BorderLayout.NORTH);
        
        bookInfoArea = new JTextArea(15, 40);
        bookInfoArea.setEditable(false);
        bookInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bookInfoArea.setBackground(new Color(248, 249, 250));
        bookInfoArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        bookInfoArea.setText("Search for a book to display its details here...");
        
        JScrollPane scrollPane = new JScrollPane(bookInfoArea);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.SOUTH);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        deleteButton = new JButton("🗑️ Delete Book");
        deleteButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteBook());
        buttonPanel.add(deleteButton);

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

    private void searchBook() {
        String searchId = searchField.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID or Unique ID", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT * FROM books WHERE unique_book_id = ? OR id = ?")) {
            
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
                    currentBookId = rs.getInt("id");
                    displayBookInfo(rs);
                    deleteButton.setEnabled(true);
                    logger.info("Book found for deletion: {}", rs.getString("unique_book_id"));
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!", "Not Found", JOptionPane.ERROR_MESSAGE);
                    clearInfo();
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching book", e);
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayBookInfo(ResultSet rs) throws SQLException {
        StringBuilder info = new StringBuilder();
        info.append("═══════════════════════════════════════════════\n");
        info.append("                    BOOK DETAILS\n");
        info.append("═══════════════════════════════════════════════\n\n");
        info.append(String.format("📌 Book ID:          %d\n", rs.getInt("id")));
        info.append(String.format("📚 Unique ID:        %s\n", rs.getString("unique_book_id")));
        info.append(String.format("📖 ISBN:             %s\n", rs.getString("isbn")));
        info.append(String.format("📕 Title:            %s\n", rs.getString("title")));
        info.append(String.format("✍️  Author:           %s\n", rs.getString("author")));
        info.append(String.format("🏷️  Genre:            %s\n", rs.getString("genre")));
        info.append(String.format("🏢 Publisher:        %s\n", rs.getString("publisher")));
        info.append(String.format("💰 Price:            ₹%.2f\n", rs.getDouble("price")));
        info.append(String.format("📊 Quantity:         %d\n", rs.getInt("quantity")));
        
        Date dateReceived = rs.getDate("date_received");
        if (dateReceived != null) {
            info.append(String.format("📅 Date Received:    %s\n", dateReceived.toString()));
        }
        
        String description = rs.getString("description");
        if (description != null && !description.isEmpty()) {
            info.append(String.format("\n📝 Description:\n%s\n", description));
        }
        
        info.append("\n═══════════════════════════════════════════════\n");
        info.append("⚠️  WARNING: This action cannot be undone!\n");
        info.append("═══════════════════════════════════════════════");
        
        bookInfoArea.setText(info.toString());
        bookInfoArea.setCaretPosition(0);
    }

    private void deleteBook() {
        if (currentBookId == -1) {
            JOptionPane.showMessageDialog(this, "Please search for a book first", "No Book Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to permanently delete this book?\nThis action cannot be undone!",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                // Check if book is currently issued
                try (PreparedStatement checkStmt = conn.prepareStatement(
                        "SELECT COUNT(*) FROM issued_books WHERE book_id = ? AND return_date IS NULL")) {
                    checkStmt.setInt(1, currentBookId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            JOptionPane.showMessageDialog(this, 
                                    "Cannot delete this book!\nIt is currently issued to members.", 
                                    "Delete Failed", 
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }

                // Delete the book
                try (PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM books WHERE id = ?")) {
                    deleteStmt.setInt(1, currentBookId);
                    int deleted = deleteStmt.executeUpdate();
                    
                    if (deleted > 0) {
                        logger.info("Book deleted successfully: ID {}", currentBookId);
                        JOptionPane.showMessageDialog(this, 
                                "✓ Book deleted successfully!", 
                                "Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                        clearInfo();
                    }
                }
            } catch (SQLException e) {
                logger.error("Error deleting book", e);
                JOptionPane.showMessageDialog(this, 
                        "Error deleting book: " + e.getMessage(), 
                        "Database Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearInfo() {
        currentBookId = -1;
        bookInfoArea.setText("Search for a book to display its details here...");
        deleteButton.setEnabled(false);
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteBook());
    }
}
