import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateBook extends JFrame {
    private JTextField idField, titleField, authorField, quantityField;
    private JButton loadButton, updateButton;

    public UpdateBook() {
        setTitle("Update Book");
        setSize(400, 350);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setBounds(30, 30, 100, 30);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(140, 30, 200, 30);
        add(idField);

        loadButton = new JButton("Load");
        loadButton.setBounds(140, 70, 100, 30);
        add(loadButton);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(30, 120, 100, 30);
        add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(140, 120, 200, 30);
        add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(30, 160, 100, 30);
        add(authorLabel);

        authorField = new JTextField();
        authorField.setBounds(140, 160, 200, 30);
        add(authorField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 200, 100, 30);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(140, 200, 200, 30);
        add(quantityField);

        updateButton = new JButton("Update");
        updateButton.setBounds(140, 250, 100, 30);
        add(updateButton);

        // Action Listeners
        loadButton.addActionListener(e -> loadBookData());
        updateButton.addActionListener(e -> updateBookData());

        setVisible(true);
    }

    private void loadBookData() {
        String bookId = idField.getText().trim();
        if (bookId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Book ID to load.");
            return;
        }

        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                titleField.setText(rs.getString("title"));
                authorField.setText(rs.getString("author"));
                quantityField.setText(String.valueOf(rs.getInt("quantity")));
            } else {
                JOptionPane.showMessageDialog(this, "Book not found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateBookData() {
        String bookId = idField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String quantityText = quantityField.getText().trim();

        if (bookId.isEmpty() || title.isEmpty() || author.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);

            String sql = "UPDATE books SET title = ?, author = ?, quantity = ? WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, title);
                pstmt.setString(2, author);
                pstmt.setInt(3, quantity);
                pstmt.setString(4, bookId);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Book updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Book not found or not updated.");
                }

            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantity must be a number.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new UpdateBook();
    }
}
