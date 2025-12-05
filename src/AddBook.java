import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddBook extends JFrame {
    private JTextField titleField, authorField, quantityField;
    private JButton addButton;

    public AddBook() {
        setTitle("Add Book");
        setSize(350, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(30, 30, 100, 30);
        add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(130, 30, 150, 30);
        add(titleField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(30, 70, 100, 30);
        add(authorLabel);

        authorField = new JTextField();
        authorField.setBounds(130, 70, 150, 30);
        add(authorField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setBounds(30, 110, 100, 30);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(130, 110, 150, 30);
        add(quantityField);

        addButton = new JButton("Add Book");
        addButton.setBounds(100, 170, 120, 30);
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBookToDatabase();
            }
        });

        setVisible(true);
    }

    private void addBookToDatabase() {
        String title = titleField.getText();
        String author = authorField.getText();
        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
            return;
        }

        String sql = "INSERT INTO books (title, author, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, quantity);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Book added successfully!");
                // Clear fields
                titleField.setText("");
                authorField.setText("");
                quantityField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add book.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new AddBook();
    }
}
