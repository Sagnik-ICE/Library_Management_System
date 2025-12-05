package com.librarymgmt.ui.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReactivateMember extends JFrame {
    private JComboBox<String> memberDropdown;
    private JTextArea memberDetails;
    private JButton reactivateButton;

    public ReactivateMember() {
        setTitle("Reactivate Member");
        setSize(400, 350);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel selectLabel = new JLabel("Select Inactive Member:");
        selectLabel.setBounds(30, 30, 200, 25);
        add(selectLabel);

        memberDropdown = new JComboBox<>();
        memberDropdown.setBounds(30, 60, 300, 30);
        add(memberDropdown);

        memberDetails = new JTextArea();
        memberDetails.setEditable(false);
        JScrollPane scroll = new JScrollPane(memberDetails);
        scroll.setBounds(30, 100, 320, 100);
        add(scroll);

        reactivateButton = new JButton("Reactivate");
        reactivateButton.setBounds(120, 220, 150, 30);
        add(reactivateButton);

        loadInactiveMembers();

        memberDropdown.addActionListener(e -> loadMemberDetails());
        reactivateButton.addActionListener(e -> reactivateSelectedMember());

        setVisible(true);
    }

    private void loadInactiveMembers() {
        memberDropdown.removeAllItems();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT m.id, m.name FROM members m WHERE m.status = 'inactive'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                memberDropdown.addItem(id + " - " + name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading inactive members: " + e.getMessage());
        }
    }

    private void loadMemberDetails() {
        String selected = (String) memberDropdown.getSelectedItem();
        if (selected == null) return;

        int memberId = Integer.parseInt(selected.split(" - ")[0]);

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT name, email, contact FROM members WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, memberId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String contact = rs.getString("contact");

                memberDetails.setText("Name: " + name + "\nEmail: " + email + "\nContact: " + contact);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading details: " + e.getMessage());
        }
    }

    private void reactivateSelectedMember() {
        String selected = (String) memberDropdown.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "No member selected.");
            return;
        }

        int memberId = Integer.parseInt(selected.split(" - ")[0]);

        try (Connection conn = DBConnection.getConnection()) {
            // Reactivate member in both tables
            String updateMembers = "UPDATE members SET status = 'active' WHERE id = ?";
            String updateUsers = "UPDATE users SET status = 'active' WHERE member_id = ?";
            PreparedStatement stmt1 = conn.prepareStatement(updateMembers);
            PreparedStatement stmt2 = conn.prepareStatement(updateUsers);

            stmt1.setInt(1, memberId);
            stmt2.setInt(1, memberId);

            stmt1.executeUpdate();
            stmt2.executeUpdate();

            JOptionPane.showMessageDialog(this, "Member reactivated successfully!");
            loadInactiveMembers(); // refresh dropdown
            memberDetails.setText("");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error reactivating member: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReactivateMember());
    }

}

