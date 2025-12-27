package book;

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
import java.text.SimpleDateFormat;

public class AddBook extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(AddBook.class);
    
    private JTextField uniqueIdField, isbnField, titleField, authorField, publisherField, priceField;
    private JComboBox<String> genreComboBox;
    private JSpinner quantitySpinner;
    private JDateChooser dateReceivedChooser;
    private JTextArea descriptionArea;
    private JLabel statusLabel;
    private JButton saveButton;

    public AddBook() {
        setTitle("Add Book - Library Management System");
        setSize(700, 800);
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
        headerPanel.setBackground(Constants.HEADER_BLUE);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Add New Book");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Enter book details below. All fields marked with * are required.");
        subtitleLabel.setForeground(new Color(220, 240, 255));
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

        // Form content with scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Unique Book ID - Required
        addFormField(formPanel, gbc, row++, "Unique Book ID *", (uniqueIdField = new JTextField(20)), 
                    "e.g., BK001");
        
        // ISBN - Optional
        addFormField(formPanel, gbc, row++, "ISBN", (isbnField = new JTextField(20)), 
                    "e.g., 978-3-16-148410-0");
        
        // Title - Required
        addFormField(formPanel, gbc, row++, "Title *", (titleField = new JTextField(20)), 
                    "Book title");
        
        // Author - Required
        addFormField(formPanel, gbc, row++, "Author *", (authorField = new JTextField(20)), 
                    "Author name");
        
        // Genre - Required
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel genreLabel = new JLabel("Genre *");
        genreLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(genreLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] genres = Constants.BOOK_GENRES;
        genreComboBox = new JComboBox<>(genres);
        genreComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(genreComboBox, gbc);
        row++;
        
        // Publisher - Optional
        addFormField(formPanel, gbc, row++, "Publisher", (publisherField = new JTextField(20)), 
                    "Publisher name");
        
        // Price - Optional
        addFormField(formPanel, gbc, row++, "Price", (priceField = new JTextField(20)), 
                    "e.g., 299.99");
        
        // Quantity - Required
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel quantityLabel = new JLabel("Quantity *");
        quantityLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(quantityLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(quantitySpinner, gbc);
        row++;
        
        // Date Received - Optional
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel dateLabel = new JLabel("Date Received");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateReceivedChooser = new JDateChooser();
        dateReceivedChooser.setDate(new java.util.Date());
        formPanel.add(dateReceivedChooser, gbc);
        row++;
        
        // Description - Optional
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(descLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        formPanel.add(descScroll, gbc);

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

        // Status label
        statusLabel = new JLabel("Ready to add book");
        statusLabel.setForeground(new Color(108, 117, 125));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerPanel.add(statusLabel, BorderLayout.WEST);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        saveButton = new JButton("💾 Save Book");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.addActionListener(e -> saveBook());
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

    private void saveBook() {
        String uniqueId = ValidationUtil.sanitizeInput(uniqueIdField.getText().trim());
        String isbn = ValidationUtil.sanitizeInput(isbnField.getText().trim());
        String title = ValidationUtil.sanitizeInput(titleField.getText().trim());
        String author = ValidationUtil.sanitizeInput(authorField.getText().trim());
        String genre = (String) genreComboBox.getSelectedItem();
        String publisher = ValidationUtil.sanitizeInput(publisherField.getText().trim());
        String priceStr = priceField.getText().trim();
        int quantity = (Integer) quantitySpinner.getValue();
        String description = descriptionArea.getText().trim();

        // Validation
        if (uniqueId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Unique Book ID is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Author is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (genre == null || genre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Genre is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isbn.isEmpty()) {
            if (isbn.length() > 0 && !ValidationUtil.isValidISBN(isbn)) {
                JOptionPane.showMessageDialog(this, "Invalid ISBN format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        double price = 0;
        if (priceStr.length() > 0) {
            try {
                price = Double.parseDouble(priceStr);
                if (price < 0) {
                    JOptionPane.showMessageDialog(this, "Price cannot be negative.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid price format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be at least 1.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database insert
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO books (unique_book_id, isbn, title, author, genre, publisher, price, quantity, date_received, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            pstmt.setString(1, uniqueId);
            pstmt.setString(2, isbn.isEmpty() ? null : isbn);
            pstmt.setString(3, title);
            pstmt.setString(4, author);
            pstmt.setString(5, genre);
            pstmt.setString(6, publisher.isEmpty() ? null : publisher);
            pstmt.setDouble(7, price);
            pstmt.setInt(8, quantity);
            pstmt.setDate(9, dateReceivedChooser.getDate() != null ? new java.sql.Date(dateReceivedChooser.getDate().getTime()) : null);
            pstmt.setString(10, description.isEmpty() ? null : description);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Book added successfully: {} by {}", title, author);
                JOptionPane.showMessageDialog(this, "✓ Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                logger.warn("Duplicate book ID: {}", uniqueId);
                JOptionPane.showMessageDialog(this, "Book ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                logger.error("Error adding book", e);
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddBook());
    }
}
