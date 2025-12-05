package com.librarymgmt.ui.user;

import com.librarymgmt.ui.admin.IssueBook;
import com.librarymgmt.ui.admin.ReturnBook;
import com.librarymgmt.ui.auth.LoginPage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserMenu extends JFrame {
    public UserMenu(String username) {
        setTitle("User Menu - Logged in as: " + username);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel with BoxLayout (vertical)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel headerLabel = new JLabel("Welcome, " + username);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(headerLabel);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 2, 20, 20)); // 2 columns
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User actions
        addButton(buttonPanel, "Search Book", e -> {
            // Open search book module
            JOptionPane.showMessageDialog(this, "Search Book module coming soon.");
        });

        addButton(buttonPanel, "Issue Book", e -> {
            new IssueBook();  // You already have this
        });

        addButton(buttonPanel, "Return Book", e -> {
            new ReturnBook();  // You already have this
        });

        addButton(buttonPanel, "View Issued Books", e -> {
            JOptionPane.showMessageDialog(this, "View issued books feature coming soon.");
        });

        addButton(buttonPanel, "Logout", e -> {
            dispose(); // Close current window
            new LoginPage(); // Go back to login page
        });

        mainPanel.add(buttonPanel);

        // ScrollPane in case future buttons increase
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane);
        setVisible(true);
    }

    private void addButton(JPanel panel, String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.addActionListener(action);
        panel.add(button);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserMenu("SampleUser"));
    }
}
