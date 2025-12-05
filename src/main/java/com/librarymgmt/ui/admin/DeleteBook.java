package com.librarymgmt.ui.admin;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteBook extends JFrame {
    private JTextField idField;
    private JButton deleteButton;

    public DeleteBook() {
        setTitle("Delete Book");
        setSize(350, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Enter Book ID:");
        idLabel.setBounds(30, 30, 100, 30);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(140, 30, 150, 30);
        add(idField);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(140, 80, 100, 30);
        add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteBook();
            }
        });

        setVisible(true);
    }

    private void deleteBook() {
        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Book ID.");
            return;
        }

        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idText);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ Book deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "❌ Book not found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new DeleteBook();
    }
}

