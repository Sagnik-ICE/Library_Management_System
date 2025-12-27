package login;

import database.DBConnection;
import transaction.ViewIssuedBooks;
import transaction.ViewFines;
import utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class UserMenu extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(UserMenu.class);
    
    private String username;
    private String memberId;
    private JLabel welcomeLabel, issuedCountLabel, overdueCountLabel, totalFinesLabel;
    private JTable issuedBooksTable;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public UserMenu(String username) {
        this.username = username;
        
        setTitle("User Dashboard - Library Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createStatisticsPanel();
        createTablesPanel();
        createFooter();
        
        loadUserData();
        logger.info("User dashboard opened for: {}", username);
        
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 135, 84));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left side
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        
        welcomeLabel = new JLabel("Welcome, " + username);
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titlePanel.add(welcomeLabel);
        
        JLabel subtitleLabel = new JLabel("Member Portal");
        subtitleLabel.setForeground(new Color(200, 230, 201));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Right side - Logout button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        rightPanel.add(logoutBtn);
        
        headerPanel.add(rightPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createStatisticsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        statsPanel.setBackground(new Color(248, 249, 250));

        // Issued Books Card
        JPanel issuedCard = createStatCard("Books Issued", "", new Color(25, 135, 84));
        issuedCountLabel = (JLabel) issuedCard.getComponent(1);
        statsPanel.add(issuedCard);

        // Overdue Books Card
        JPanel overdueCard = createStatCard("Overdue Books", "", new Color(255, 193, 7));
        overdueCountLabel = (JLabel) overdueCard.getComponent(1);
        statsPanel.add(overdueCard);

        // Pending Fines Card
        JPanel finesCard = createStatCard("Pending Fines", "", new Color(220, 53, 69));
        totalFinesLabel = (JLabel) finesCard.getComponent(1);
        statsPanel.add(finesCard);

        add(statsPanel, BorderLayout.NORTH);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(color);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        card.add(titleLabel);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(valueLabel);

        return card;
    }

    private void createTablesPanel() {
        JPanel tablesPanel = new JPanel(new BorderLayout());
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Issued Books Table
        String[] columnNames = {"ID", "Book Title", "Issue Date", "Due Date", "Days Remaining", "Status"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        issuedBooksTable = new JTable(tableModel);
        issuedBooksTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        issuedBooksTable.setRowHeight(25);
        issuedBooksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        issuedBooksTable.setShowGrid(true);
        issuedBooksTable.setGridColor(new Color(220, 220, 220));

        JTableHeader header = issuedBooksTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(25, 135, 84));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        // Column widths
        issuedBooksTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        issuedBooksTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        issuedBooksTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        issuedBooksTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        issuedBooksTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        issuedBooksTable.getColumnModel().getColumn(5).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(issuedBooksTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        
        JPanel tableHeader = new JPanel(new BorderLayout());
        JLabel tableTitle = new JLabel("My Issued Books");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        tableHeader.add(tableTitle, BorderLayout.WEST);
        tableHeader.setOpaque(false);
        
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(tableHeader, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        tablesPanel.add(tableContainer, BorderLayout.CENTER);
        add(tablesPanel, BorderLayout.CENTER);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton viewFinesButton = new JButton("View My Fines");
        viewFinesButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        viewFinesButton.setForeground(Color.WHITE);
        viewFinesButton.setBackground(new Color(220, 53, 69));
        viewFinesButton.setFocusPainted(false);
        viewFinesButton.setBorderPainted(false);
        viewFinesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewFinesButton.addActionListener(e -> new ViewFines());
        footerPanel.add(viewFinesButton);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(70, 130, 180));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadUserData());
        footerPanel.add(refreshButton);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadUserData() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT id FROM members WHERE unique_member_id = ?")) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                memberId = String.valueOf(rs.getInt("id"));
                loadIssuedBooks();
                loadStatistics();
            }
        } catch (SQLException e) {
            logger.error("Error loading user data", e);
        }
    }

    private void loadIssuedBooks() {
        DefaultTableModel tableModel = (DefaultTableModel) issuedBooksTable.getModel();
        tableModel.setRowCount(0);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT ib.id, b.title, ib.issue_date, ib.due_date " +
                    "FROM issued_books ib JOIN books b ON ib.book_id = b.id " +
                    "WHERE ib.member_id = ? AND ib.return_date IS NULL " +
                    "ORDER BY ib.due_date ASC")) {
            
            pstmt.setInt(1, Integer.parseInt(memberId));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                java.util.Date dueDate = rs.getDate("due_date");
                java.util.Date today = new java.util.Date();
                long daysRemaining = (dueDate.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
                
                String status = daysRemaining < 0 ? "Overdue" : "On Time";
                
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("title"),
                    dateFormat.format(rs.getDate("issue_date")),
                    dateFormat.format(dueDate),
                    Math.max(0, daysRemaining) + " days",
                    status
                });
            }
        } catch (SQLException e) {
            logger.error("Error loading issued books", e);
        }
    }

    private void loadStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            // Count issued books
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM issued_books WHERE member_id = ? AND return_date IS NULL")) {
                pstmt.setInt(1, Integer.parseInt(memberId));
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    issuedCountLabel.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Count overdue books
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM issued_books WHERE member_id = ? AND return_date IS NULL AND due_date < CURDATE()")) {
                pstmt.setInt(1, Integer.parseInt(memberId));
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    overdueCountLabel.setText(String.valueOf(rs.getInt(1)));
                }
            }

            // Sum pending fines
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT COALESCE(SUM(amount), 0) FROM fines WHERE member_id = ? AND status = 'pending'")) {
                pstmt.setInt(1, Integer.parseInt(memberId));
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    totalFinesLabel.setText(String.format("Rs. %.2f", rs.getDouble(1)));
                }
            }
        } catch (SQLException e) {
            logger.error("Error loading statistics", e);
        }
    }

    private void logout() {
        int option = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            logger.info("User logged out: {}", username);
            dispose();
            new LoginPage();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserMenu("demo"));
    }
}

