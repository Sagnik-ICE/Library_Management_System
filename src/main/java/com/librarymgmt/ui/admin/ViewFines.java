package com.librarymgmt.ui.admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ViewFines extends JFrame {
    JTable table;

    public ViewFines() {
        setTitle("Overdue Fines");
        setSize(700, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Returned Books with Fines");
        label.setBounds(20, 10, 300, 30);
        add(label);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 50, 640, 280);
        add(scrollPane);

        loadFines();

        setVisible(true);
    }

    private void loadFines() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Issue ID");
        model.addColumn("Book ID");
        model.addColumn("Member ID");
        model.addColumn("Days Overdue");
        model.addColumn("Fine (₹)");

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM issued_books WHERE return_date IS NOT NULL")) {

            while (rs.next()) {
                LocalDate issueDate = rs.getDate("issue_date").toLocalDate();
                LocalDate returnDate = rs.getDate("return_date").toLocalDate();
                long daysBetween = ChronoUnit.DAYS.between(issueDate, returnDate);
                long overdue = daysBetween - 14;  // 14-day grace period
                long fine = overdue > 0 ? overdue * 10 : 0;

                if (fine > 0) {
                    model.addRow(new Object[]{
                            rs.getInt("issue_id"),
                            rs.getInt("book_id"),
                            rs.getInt("member_id"),
                            overdue,
                            fine
                    });
                }
            }

            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading fines: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewFines();
    }
}

