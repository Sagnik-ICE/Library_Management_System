package com.librarymgmt.ui.admin;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class ReturnBook extends JFrame {
    private JTextField bookIdField, memberIdField;
    private JButton returnButton;

    public ReturnBook() {
        setTitle("Return Book");
        setSize(350, 230);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel bookLabel = new JLabel("Book ID:");
        bookLabel.setBounds(30, 40, 100, 25);
        add(bookLabel);

        bookIdField = new JTextField();
        bookIdField.setBounds(120, 40, 150, 25);
        add(bookIdField);

        JLabel memberLabel = new JLabel("Member ID:");
        memberLabel.setBounds(30, 80, 100, 25);
        add(memberLabel);

        memberIdField = new JTextField();
        memberIdField.setBounds(120, 80, 150, 25);
        add(memberIdField);

        returnButton = new JButton("Return Book");
        returnButton.setBounds(100, 130, 120, 30);
        add(returnButton);

        returnButton.addActionListener(e -> returnBook());

        setVisible(true);
    }

    private void returnBook() {
        String bookIdStr = bookIdField.getText().trim();
        String memberIdStr = memberIdField.getText().trim();

        if (bookIdStr.isEmpty() || memberIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Book ID and Member ID.");
            return;
        }

        int bookId, memberId;
        try {
            bookId = Integer.parseInt(bookIdStr);
            memberId = Integer.parseInt(memberIdStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID and Member ID must be numeric.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            // 1. Check if there's a pending issue
            String fetchSql = "SELECT issue_date FROM issued_books WHERE book_id = ? AND member_id = ? AND return_date IS NULL";
            PreparedStatement fetchStmt = conn.prepareStatement(fetchSql);
            fetchStmt.setInt(1, bookId);
            fetchStmt.setInt(2, memberId);
            ResultSet rs = fetchStmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No active issue record found for this Book ID and Member ID.");
                return;
            }

            Date issueDate = rs.getDate("issue_date");
            Date returnDate = Date.valueOf(LocalDate.now());

            // 2. Calculate fine
            long diffDays = (returnDate.getTime() - issueDate.getTime()) / (1000 * 60 * 60 * 24);
            double fine = diffDays > 14 ? (diffDays - 14) * 2.0 : 0.0;  // 2 units per extra day

            // 3. Update issued_books: mark return_date
            String updateIssueSql = "UPDATE issued_books SET return_date = ? WHERE book_id = ? AND member_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateIssueSql);
            updateStmt.setDate(1, returnDate);
            updateStmt.setInt(2, bookId);
            updateStmt.setInt(3, memberId);
            updateStmt.executeUpdate();

            // 4. Insert into returned_books (full history)
            String insertReturnSql = "INSERT INTO returned_books (book_id, member_id, issue_date, return_date, fine) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertReturnSql);
            insertStmt.setInt(1, bookId);
            insertStmt.setInt(2, memberId);
            insertStmt.setDate(3, issueDate);
            insertStmt.setDate(4, returnDate);
            insertStmt.setDouble(5, fine);
            insertStmt.executeUpdate();

            // 5. Update quantity in books table
            String updateQtySql = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
            PreparedStatement qtyStmt = conn.prepareStatement(updateQtySql);
            qtyStmt.setInt(1, bookId);
            qtyStmt.executeUpdate();

            // 6. Insert fine into fines table (only if fine > 0)
            if (fine > 0) {
                String insertFineSql = "INSERT INTO fines (member_id, book_id, amount, paid, fine_date) VALUES (?, ?, ?, false, ?)";
                PreparedStatement fineStmt = conn.prepareStatement(insertFineSql);
                fineStmt.setInt(1, memberId);
                fineStmt.setInt(2, bookId);
                fineStmt.setDouble(3, fine);
                fineStmt.setDate(4, returnDate);
                fineStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Book returned successfully.\nFine: " + fine);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ReturnBook();
    }
}

