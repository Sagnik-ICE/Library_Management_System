package com.librarymgmt.ui.admin;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class IssueBook extends JFrame {
    private JTextField bookIdField, memberIdField;
    private JButton issueButton;

    public IssueBook() {
        setTitle("Issue Book");
        setSize(350, 250);
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

        issueButton = new JButton("Issue Book");
        issueButton.setBounds(100, 130, 120, 30);
        add(issueButton);

        issueButton.addActionListener(e -> issueBook());

        setVisible(true);
    }

    private void issueBook() {
        String bookIdText = bookIdField.getText().trim();
        String memberIdText = memberIdField.getText().trim();

        if (bookIdText.isEmpty() || memberIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Book ID and Member ID.");
            return;
        }

        int bookId, memberId;
        try {
            bookId = Integer.parseInt(bookIdText);
            memberId = Integer.parseInt(memberIdText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID and Member ID must be numeric.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            // Check if member exists and is active
            PreparedStatement checkMember = conn.prepareStatement("SELECT status FROM members WHERE id = ?");
            checkMember.setInt(1, memberId);
            ResultSet rsMember = checkMember.executeQuery();
            if (!rsMember.next()) {
                JOptionPane.showMessageDialog(this, "Member ID not found.");
                return;
            } else if (!"active".equalsIgnoreCase(rsMember.getString("status"))) {
                JOptionPane.showMessageDialog(this, "Member is inactive and cannot issue books.");
                return;
            }

            // Check if book exists and quantity > 0
            PreparedStatement checkBook = conn.prepareStatement("SELECT quantity FROM books WHERE id = ?");
            checkBook.setInt(1, bookId);
            ResultSet rsBook = checkBook.executeQuery();
            if (!rsBook.next()) {
                JOptionPane.showMessageDialog(this, "Book ID not found.");
                return;
            }
            int quantity = rsBook.getInt("quantity");
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Book is not available.");
                return;
            }

            // Optional: Prevent issuing same book multiple times to same user without return
            PreparedStatement checkDuplicate = conn.prepareStatement(
                    "SELECT * FROM issued_books WHERE book_id = ? AND member_id = ? AND return_date IS NULL");
            checkDuplicate.setInt(1, bookId);
            checkDuplicate.setInt(2, memberId);
            ResultSet rsDup = checkDuplicate.executeQuery();
            if (rsDup.next()) {
                JOptionPane.showMessageDialog(this, "This book is already issued to this member.");
                return;
            }

            // Issue book
            PreparedStatement issueStmt = conn.prepareStatement(
                    "INSERT INTO issued_books (book_id, member_id, issue_date) VALUES (?, ?, ?)");
            issueStmt.setInt(1, bookId);
            issueStmt.setInt(2, memberId);
            issueStmt.setDate(3, Date.valueOf(LocalDate.now()));

            int issued = issueStmt.executeUpdate();
            if (issued > 0) {
                // Update book quantity
                PreparedStatement updateQty = conn.prepareStatement("UPDATE books SET quantity = quantity - 1 WHERE id = ?");
                updateQty.setInt(1, bookId);
                updateQty.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book issued successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to issue book.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new IssueBook();
    }
}

