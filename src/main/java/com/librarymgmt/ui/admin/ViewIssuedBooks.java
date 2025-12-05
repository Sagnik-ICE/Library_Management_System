package com.librarymgmt.ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewIssuedBooks extends JFrame {
    JTable table;

    public ViewIssuedBooks() {
        setTitle("Issued Books");
        setSize(700, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("All Issued Books");
        label.setBounds(20, 10, 200, 30);
        add(label);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 50, 640, 280);
        add(scrollPane);

        loadIssuedBooks();

        setVisible(true);
    }

    private void loadIssuedBooks() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Issue ID");
        model.addColumn("Book ID");
        model.addColumn("Member ID");
        model.addColumn("Issue Date");
        model.addColumn("Return Date");

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM issued_books")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("issue_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("issue_date"),
                        rs.getDate("return_date") != null ? rs.getDate("return_date") : "Not Returned"
                });
            }

            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading issued books: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewIssuedBooks();
    }
}

