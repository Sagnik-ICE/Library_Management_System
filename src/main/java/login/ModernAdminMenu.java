package login;

import database.DBConnection;
import utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;

public class ModernAdminMenu extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ModernAdminMenu.class);
    
    private JLabel totalBooksLabel, totalMembersLabel, issuedBooksLabel, overdueLabel;
    private String adminUsername;

    public ModernAdminMenu(String username) {
        this.adminUsername = username;
        
        setTitle("Library Management System - Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null);
        
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("Could not set system look and feel", e);
        }

        // Create components
        createHeader();
        createStatisticsPanel();
        createMainContent();
        createFooter();
        
        // Load statistics
        loadDashboardStats();
        
        logger.info("Admin dashboard opened for user: {}", username);
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Constants.HEADER_BLUE);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Title on the left
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel("📚");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        titlePanel.add(iconLabel);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        textPanel.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Administrator Dashboard");
        subtitleLabel.setForeground(new Color(220, 240, 255));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPanel.add(subtitleLabel);
        
        titlePanel.add(textPanel);
        headerPanel.add(titlePanel, BorderLayout.WEST);

        // User info on the right
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel("👤 " + adminUsername);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userPanel.add(userLabel);
        
        JButton logoutBtn = createStyledButton("Logout", new Color(220, 53, 69));
        logoutBtn.addActionListener(e -> logout());
        userPanel.add(logoutBtn);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createStatisticsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setBackground(new Color(248, 249, 250));

        // Total Books Card
        JPanel booksCard = createStatCard("📚 Total Books", "Loading...", new Color(13, 110, 253));
        totalBooksLabel = (JLabel) ((JPanel) booksCard.getComponent(1)).getComponent(0);
        statsPanel.add(booksCard);

        // Total Members Card
        JPanel membersCard = createStatCard("👥 Total Members", "Loading...", new Color(25, 135, 84));
        totalMembersLabel = (JLabel) ((JPanel) membersCard.getComponent(1)).getComponent(0);
        statsPanel.add(membersCard);

        // Issued Books Card
        JPanel issuedCard = createStatCard("📖 Issued Books", "Loading...", new Color(255, 193, 7));
        issuedBooksLabel = (JLabel) ((JPanel) issuedCard.getComponent(1)).getComponent(0);
        statsPanel.add(issuedCard);

        // Overdue Books Card
        JPanel overdueCard = createStatCard("⚠️ Overdue Books", "Loading...", new Color(220, 53, 69));
        overdueLabel = (JLabel) ((JPanel) overdueCard.getComponent(1)).getComponent(0);
        statsPanel.add(overdueCard);

        add(statsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(108, 117, 125));
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        valuePanel.setOpaque(false);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valuePanel.add(valueLabel);
        
        card.add(valuePanel, BorderLayout.CENTER);

        return card;
    }

    private void createMainContent() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));

        // Book Management Panel
        JPanel bookPanel = createSectionPanel("📚 Book Management", new Color(13, 110, 253));
        addMenuButton(bookPanel, "➕ Add Book", e -> launchPage("book.AddBook"));
        addMenuButton(bookPanel, "📋 View Books", e -> launchPage("book.ViewBooks"));
        addMenuButton(bookPanel, "✏️ Update Book", e -> launchPage("book.UpdateBook"));
        addMenuButton(bookPanel, "🗑️ Delete Book", e -> launchPage("book.DeleteBook"));
        addMenuButton(bookPanel, "🔍 Search Book", e -> launchPage("book.SearchBook"));
        bookPanel.add(Box.createVerticalGlue());
        mainPanel.add(bookPanel);

        // Member Management Panel
        JPanel memberPanel = createSectionPanel("👥 Member Management", new Color(25, 135, 84));
        addMenuButton(memberPanel, "➕ Add Member", e -> launchPage("member.AddMember"));
        addMenuButton(memberPanel, "📋 View Members", e -> launchPage("member.ViewMembers"));
        addMenuButton(memberPanel, "✏️ Update Member", e -> launchPage("member.UpdateMember"));
        addMenuButton(memberPanel, "🔍 Search Member", e -> launchPage("member.SearchMember"));
        addMenuButton(memberPanel, "⛔ Deactivate Member", e -> launchPage("member.DeactivateMember"));
        addMenuButton(memberPanel, "✅ Reactivate Member", e -> launchPage("member.ReactivateMember"));
        memberPanel.add(Box.createVerticalGlue());
        mainPanel.add(memberPanel);

        // Transaction Management Panel
        JPanel transactionPanel = createSectionPanel("📖 Transaction Management", new Color(255, 193, 7));
        addMenuButton(transactionPanel, "📤 Issue Book", e -> launchPage("transaction.IssueBook"));
        addMenuButton(transactionPanel, "📥 Return Book", e -> launchPage("transaction.ReturnBook"));
        addMenuButton(transactionPanel, "📚 View Issued Books", e -> launchPage("transaction.ViewIssuedBooks"));
        addMenuButton(transactionPanel, "✅ View Returned Books", e -> launchPage("transaction.ViewReturnedBooks"));
        addMenuButton(transactionPanel, "💰 View Fines", e -> launchPage("transaction.ViewFines"));
        transactionPanel.add(Box.createVerticalGlue());
        mainPanel.add(transactionPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createSectionPanel(String title, Color accentColor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panel.add(separator);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        return panel;
    }

    private void addMenuButton(JPanel panel, String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(new Color(33, 37, 41));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(248, 249, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
        
        button.addActionListener(action);
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel footerLabel = new JLabel("Library Management System v2.0.0 | © 2025");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(108, 117, 125));
        footerPanel.add(footerLabel);
        
        JButton refreshBtn = createStyledButton("🔄 Refresh Stats", new Color(13, 110, 253));
        refreshBtn.addActionListener(e -> loadDashboardStats());
        footerPanel.add(refreshBtn);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void loadDashboardStats() {
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            int totalBooks = 0, totalMembers = 0, issuedBooks = 0, overdueBooks = 0;
            
            @Override
            protected Void doInBackground() {
                try (Connection conn = DBConnection.getConnection()) {
                    // Total Books
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM books")) {
                        if (rs.next()) totalBooks = rs.getInt(1);
                    }
                    
                    // Total Members
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM members")) {
                        if (rs.next()) totalMembers = rs.getInt(1);
                    }
                    
                    // Issued Books
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM issued_books")) {
                        if (rs.next()) issuedBooks = rs.getInt(1);
                    }
                    
                    // Overdue Books
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(
                             "SELECT COUNT(*) FROM issued_books WHERE return_date < CURDATE() AND actual_return_date IS NULL")) {
                        if (rs.next()) overdueBooks = rs.getInt(1);
                    }
                    
                    logger.info("Dashboard stats loaded: Books={}, Members={}, Issued={}, Overdue={}", 
                               totalBooks, totalMembers, issuedBooks, overdueBooks);
                } catch (SQLException e) {
                    logger.error("Error loading dashboard statistics", e);
                }
                return null;
            }
            
            @Override
            protected void done() {
                totalBooksLabel.setText(String.valueOf(totalBooks));
                totalMembersLabel.setText(String.valueOf(totalMembers));
                issuedBooksLabel.setText(String.valueOf(issuedBooks));
                overdueLabel.setText(String.valueOf(overdueBooks));
            }
        };
        
        worker.execute();
    }

    private void launchForm(Class<?> clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (instance instanceof JDialog) {
                JDialog dialog = (JDialog) instance;
                dialog.setModal(true);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            } else if (instance instanceof JFrame) {
                JFrame frame = (JFrame) instance;
                frame.setLocationRelativeTo(this);
                frame.setVisible(true);
            }
            logger.info("Launched form: {}", clazz.getSimpleName());
        } catch (Exception e) {
            logger.error("Error launching form: " + clazz.getSimpleName(), e);
            JOptionPane.showMessageDialog(this, 
                "Error opening " + clazz.getSimpleName() + ": " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void launchPage(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (instance instanceof JFrame) {
                JFrame frame = (JFrame) instance;
                frame.setLocationRelativeTo(this);
                frame.setVisible(true);
            }
            logger.info("Launched page: {}", className);
        } catch (Exception e) {
            logger.error("Error launching page: " + className, e);
            JOptionPane.showMessageDialog(this, 
                "Error opening page: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            logger.info("User {} logged out", adminUsername);
            dispose();
            new LoginPage();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ModernAdminMenu("admin"));
    }
}
