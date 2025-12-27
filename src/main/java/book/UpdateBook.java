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

public class UpdateBook extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(UpdateBook.class);
    
    private JTextField searchField, uniqueIdField, isbnField, titleField, authorField, publisherField, priceField;
    private JComboBox<String> genreComboBox;
    private JSpinner quantitySpinner;
    private JDateChooser dateReceivedChooser;
    private JTextArea descriptionArea;
    private JButton searchButton, updateButton;
    private int currentBookId = -1;

    public UpdateBook() {
        setTitle("Update Book - Library Management System");
        setSize(700, 800);
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
        headerPanel.setBackground(Constants.HEADER_BLUE);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Update Book");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Search and update book information");
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

    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        searchPanel.setBackground(new Color(248, 249, 250));
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));
        
        JLabel searchLabel = new JLabel("Enter Book ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchLabel.setForeground(new Color(33, 37, 41));
        searchPanel.add(searchLabel);
        
        searchField = new JTextField(20);
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
        
        add(searchPanel, BorderLayout.NORTH);
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

        addFormField(formPanel, gbc, row++, "Unique Book ID", (uniqueIdField = new JTextField(20)), "Unique identifier");
        uniqueIdField.setEnabled(false);
        
        addFormField(formPanel, gbc, row++, "ISBN", (isbnField = new JTextField(20)), "ISBN number");
        addFormField(formPanel, gbc, row++, "Title *", (titleField = new JTextField(20)), "Book title");
        addFormField(formPanel, gbc, row++, "Author *", (authorField = new JTextField(20)), "Author name");
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel genreLabel = new JLabel("Genre *");
        genreLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        genreLabel.setForeground(new Color(33, 37, 41));
        formPanel.add(genreLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        genreComboBox = new JComboBox<>(Constants.BOOK_GENRES);
        genreComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(genreComboBox, gbc);
        row++;
        
        addFormField(formPanel, gbc, row++, "Publisher", (publisherField = new JTextField(20)), "Publisher name");
        addFormField(formPanel, gbc, row++, "Price", (priceField = new JTextField(20)), "Book price");
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel qtyLabel = new JLabel("Quantity *");
        qtyLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formPanel.add(qtyLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 0, 1000, 1));
        quantitySpinner.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formPanel.add(quantitySpinner, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel dateLabel = new JLabel("Date Received");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        dateLabel.setForeground(new Color(33, 37, 41));
        formPanel.add(dateLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dateReceivedChooser = new JDateChooser();
        formPanel.add(dateReceivedChooser, gbc);
        row++;
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel descLabel = new JLabel("Description");
        descLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        descLabel.setForeground(new Color(33, 37, 41));
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
        labelComp.setForeground(new Color(33, 37, 41));
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
        
        updateButton = new JButton("Update Book");
        updateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        updateButton.setForeground(Color.WHITE);
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setFocusPainted(false);
        updateButton.setBorderPainted(false);
        updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateButton.setEnabled(false);
        updateButton.addActionListener(e -> updateBook());
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

    private void searchBook() {
        String searchId = searchField.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID", "Input Required", JOptionPane.WARNING_MESSAGE);
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
                    uniqueIdField.setText(rs.getString("unique_book_id"));
                    isbnField.setText(rs.getString("isbn"));
                    titleField.setText(rs.getString("title"));
                    authorField.setText(rs.getString("author"));
                    genreComboBox.setSelectedItem(rs.getString("genre"));
                    publisherField.setText(rs.getString("publisher"));
                    priceField.setText(String.valueOf(rs.getDouble("price")));
                    quantitySpinner.setValue(rs.getInt("quantity"));
                    
                    Date dateReceived = rs.getDate("date_received");
                    if (dateReceived != null) {
                        dateReceivedChooser.setDate(new java.util.Date(dateReceived.getTime()));
                    }
                    
                    descriptionArea.setText(rs.getString("description"));
                    
                    setFormEnabled(true);
                    updateButton.setEnabled(true);
                    
                    logger.info("Book loaded for update: {}", uniqueIdField.getText());
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found!", "Not Found", JOptionPane.ERROR_MESSAGE);
                    clearForm();
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching book", e);
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBook() {
        if (currentBookId == -1) {
            JOptionPane.showMessageDialog(this, "Please search for a book first", "No Book Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String genre = (String) genreComboBox.getSelectedItem();
        String publisher = publisherField.getText().trim();
        String priceStr = priceField.getText().trim();
        int quantity = (Integer) quantitySpinner.getValue();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Author are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price = 0;
        if (!priceStr.isEmpty()) {
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid price format", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE books SET isbn=?, title=?, author=?, genre=?, publisher=?, price=?, quantity=?, date_received=?, description=? WHERE id=?")) {
            
            pstmt.setString(1, isbn.isEmpty() ? null : isbn);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, genre);
            pstmt.setString(5, publisher.isEmpty() ? null : publisher);
            pstmt.setDouble(6, price);
            pstmt.setInt(7, quantity);
            pstmt.setDate(8, dateReceivedChooser.getDate() != null ? new java.sql.Date(dateReceivedChooser.getDate().getTime()) : null);
            pstmt.setString(9, description.isEmpty() ? null : description);
            pstmt.setInt(10, currentBookId);

            int updated = pstmt.executeUpdate();
            if (updated > 0) {
                logger.info("Book updated successfully: {}", title);
                JOptionPane.showMessageDialog(this, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            }
        } catch (SQLException e) {
            logger.error("Error updating book", e);
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setFormEnabled(boolean enabled) {
        isbnField.setEnabled(enabled);
        titleField.setEnabled(enabled);
        authorField.setEnabled(enabled);
        genreComboBox.setEnabled(enabled);
        publisherField.setEnabled(enabled);
        priceField.setEnabled(enabled);
        quantitySpinner.setEnabled(enabled);
        dateReceivedChooser.setEnabled(enabled);
        descriptionArea.setEnabled(enabled);
    }

    private void clearForm() {
        currentBookId = -1;
        uniqueIdField.setText("");
        isbnField.setText("");
        titleField.setText("");
        authorField.setText("");
        genreComboBox.setSelectedIndex(0);
        publisherField.setText("");
        priceField.setText("");
        quantitySpinner.setValue(1);
        dateReceivedChooser.setDate(null);
        descriptionArea.setText("");
        setFormEnabled(false);
        updateButton.setEnabled(false);
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateBook());
    }
}
