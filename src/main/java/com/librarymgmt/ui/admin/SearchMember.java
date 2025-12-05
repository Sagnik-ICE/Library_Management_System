package com.librarymgmt.ui.admin;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class SearchMember extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JTextArea resultArea;

    public SearchMember() {
        setTitle("Search Member");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Enter Name or Email:");
        label.setBounds(30, 30, 150, 30);
        add(label);

        searchField = new JTextField();
        searchField.setBounds(180, 30, 170, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(130, 80, 120, 30);
        add(searchButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setBounds(30, 130, 320, 100);
        add(resultArea);

        searchButton.addActionListener(e -> searchMember());

        setVisible(true);
    }

    private void searchMember() {
        String input = searchField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name or email.");
            return;
        }

        String sql = "SELECT * FROM members WHERE name = ? OR email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, input);
            pstmt.setString(2, input);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String output = "ID: " + rs.getInt("id") + "\n"
                        + "Name: " + rs.getString("name") + "\n"
                        + "Email: " + rs.getString("email") + "\n"
                        + "Contact: " + rs.getString("contact");
                resultArea.setText(output);
            } else {
                resultArea.setText("No member found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new SearchMember();
    }
}

