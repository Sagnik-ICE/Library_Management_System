package transaction;

import database.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewFines extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ViewFines.class);
    
    private JTable finesTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JComboBox<String> statusFilterCombo;
    
    private final String[] columnNames = {"Fine ID", "Member Name", "Book Title", "Fine Amount", "Fine Reason", "Status", "Payment Date"};

    public ViewFines() {
        setTitle("Fines Management - Library Management System");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createFilterPanel();
        createTablePanel();
        createFooter();
        
        loadFines("all");
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(220, 53, 69));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Fines Management");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Track and manage member fines and payments");
        subtitleLabel.setForeground(new Color(255, 200, 200));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(subtitleLabel);
        
        headerPanel.add(textPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filterPanel.setBackground(new Color(248, 249, 250));
        filterPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));
        
        JLabel filterLabel = new JLabel("Status Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterPanel.add(filterLabel);
        
        String[] statusOptions = {"All Fines", "Unpaid Only", "Paid Only"};
        statusFilterCombo = new JComboBox<>(statusOptions);
        statusFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusFilterCombo.setPreferredSize(new Dimension(140, 30));
        statusFilterCombo.addActionListener(e -> applyFilter());
        filterPanel.add(statusFilterCombo);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(220, 53, 69));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> applyFilter());
        filterPanel.add(refreshButton);
        
        JPanel combinedPanel = new JPanel(new BorderLayout());
        JPanel header = (JPanel) getContentPane().getComponent(0);
        combinedPanel.add(header, BorderLayout.NORTH);
        combinedPanel.add(filterPanel, BorderLayout.SOUTH);
        add(combinedPanel, BorderLayout.NORTH);
    }

    private void createTablePanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        finesTable = new JTable(tableModel);
        finesTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        finesTable.setRowHeight(28);
        finesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        finesTable.setGridColor(new Color(222, 226, 230));
        finesTable.setShowGrid(true);
        
        JTableHeader header = finesTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(220, 53, 69));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        finesTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        finesTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        finesTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        finesTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        finesTable.getColumnModel().getColumn(4).setPreferredWidth(200);
        finesTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        finesTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(finesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(222, 226, 230)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        statusLabel = new JLabel("0 fines found");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(108, 117, 125));
        footerPanel.add(statusLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton recordPaymentButton = new JButton("Record Payment");
        recordPaymentButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        recordPaymentButton.setForeground(Color.WHITE);
        recordPaymentButton.setBackground(new Color(25, 135, 84));
        recordPaymentButton.setFocusPainted(false);
        recordPaymentButton.setBorderPainted(false);
        recordPaymentButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        recordPaymentButton.addActionListener(e -> recordPayment());
        buttonPanel.add(recordPaymentButton);
        
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setBackground(new Color(13, 110, 253));
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setBorderPainted(false);
        viewDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewDetailsButton.addActionListener(e -> viewFineDetails());
        buttonPanel.add(viewDetailsButton);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(108, 117, 125));
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        footerPanel.add(buttonPanel, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void applyFilter() {
        String selectedFilter = (String) statusFilterCombo.getSelectedItem();
        if ("All Fines".equals(selectedFilter)) {
            loadFines("all");
        } else if ("Unpaid Only".equals(selectedFilter)) {
            loadFines("unpaid");
        } else if ("Paid Only".equals(selectedFilter)) {
            loadFines("paid");
        }
    }

    private void loadFines(String filter) {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> fines = new ArrayList<>();
                
                StringBuilder query = new StringBuilder();
                query.append("SELECT f.id, CONCAT(m.first_name, ' ', m.last_name) AS member_name, ");
                query.append("b.title, f.fine_amount, f.fine_reason, f.fine_status, f.payment_date ");
                query.append("FROM fines f ");
                query.append("JOIN members m ON f.member_id = m.id ");
                query.append("JOIN books b ON f.book_id = b.id ");
                query.append("WHERE 1=1 ");
                
                if ("unpaid".equals(filter)) {
                    query.append("AND f.fine_status = 'unpaid' ");
                } else if ("paid".equals(filter)) {
                    query.append("AND f.fine_status = 'paid' ");
                }
                
                query.append("ORDER BY f.fine_status ASC, f.id DESC");
                
                try (Connection conn = DBConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query.toString())) {
                    
                    while (rs.next()) {
                        int fineId = rs.getInt("id");
                        String memberName = rs.getString("member_name");
                        String bookTitle = rs.getString("title");
                        double fineAmount = rs.getDouble("fine_amount");
                        String fineReason = rs.getString("fine_reason");
                        String fineStatus = rs.getString("fine_status");
                        Date paymentDate = rs.getDate("payment_date");
                        
                        String statusDisplay = fineStatus.toUpperCase();
                        String paymentDateStr = paymentDate != null ? paymentDate.toString() : "N/A";
                        
                        fines.add(new Object[]{
                                fineId,
                                memberName,
                                bookTitle,
                                "₹" + String.format("%.2f", fineAmount),
                                fineReason,
                                statusDisplay,
                                paymentDateStr
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return fines;
            }
            
            @Override
            protected void done() {
                try {
                    List<Object[]> fines = get();
                    tableModel.setRowCount(0);
                    for (Object[] fine : fines) {
                        tableModel.addRow(fine);
                    }
                    
                    double totalFines = 0;
                    double unpaidFines = 0;
                    int unpaidCount = 0;
                    
                    for (Object[] fine : fines) {
                        String amountStr = fine[3].toString().replace("₹", "");
                        double amount = Double.parseDouble(amountStr);
                        totalFines += amount;
                        
                        if ("UNPAID".equals(fine[5])) {
                            unpaidFines += amount;
                            unpaidCount++;
                        }
                    }
                    
                    statusLabel.setText(fines.size() + " fine(s) | Total: ₹" + String.format("%.2f", totalFines) + 
                            " | Unpaid: " + unpaidCount + " (₹" + String.format("%.2f", unpaidFines) + ")");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void recordPayment() {
        int selectedRow = finesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a fine to record payment", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String status = tableModel.getValueAt(selectedRow, 5).toString();
        if ("PAID".equals(status)) {
            JOptionPane.showMessageDialog(this, "This fine has already been paid!", "Already Paid", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int fineId = (int) tableModel.getValueAt(selectedRow, 0);
        String memberName = tableModel.getValueAt(selectedRow, 1).toString();
        String fineAmount = tableModel.getValueAt(selectedRow, 3).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Record payment for this fine?\n\n" +
                "Member: " + memberName + "\n" +
                "Amount: " + fineAmount + "\n\n" +
                "This action will mark the fine as PAID.", 
                "Confirm Payment", 
                JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE fines SET fine_status = 'paid', payment_date = ? WHERE id = ?")) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, fineId);
            
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                logger.info("Fine payment recorded: FineID={}, Amount={}", fineId, fineAmount);
                JOptionPane.showMessageDialog(this, 
                        "✅ Payment recorded successfully!\n\nFine ID: " + fineId, 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                applyFilter();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to record payment", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.error("Error recording payment: {}", e.getMessage());
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewFineDetails() {
        int selectedRow = finesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a fine to view details", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int fineId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT f.*, CONCAT(m.first_name, ' ', m.last_name) AS member_name, " +
                     "m.email, m.contact_number, b.title, b.isbn " +
                     "FROM fines f " +
                     "JOIN members m ON f.member_id = m.id " +
                     "JOIN books b ON f.book_id = b.id " +
                     "WHERE f.id = ?")) {
            pstmt.setInt(1, fineId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    StringBuilder details = new StringBuilder();
                    details.append("═══════════════════════════════════════\n");
                    details.append("           FINE DETAILS\n");
                    details.append("═══════════════════════════════════════\n\n");
                    details.append("🆔 Fine ID: ").append(rs.getInt("id")).append("\n\n");
                    details.append("👤 Member: ").append(rs.getString("member_name")).append("\n");
                    details.append("📧 Email: ").append(rs.getString("email")).append("\n");
                    details.append("📱 Contact: ").append(rs.getString("contact_number")).append("\n\n");
                    details.append("📖 Book: ").append(rs.getString("title")).append("\n");
                    details.append("🔖 ISBN: ").append(rs.getString("isbn")).append("\n\n");
                    details.append("💰 Fine Amount: ₹").append(String.format("%.2f", rs.getDouble("fine_amount"))).append("\n");
                    details.append("📝 Reason: ").append(rs.getString("fine_reason")).append("\n");
                    details.append("📊 Status: ").append(rs.getString("fine_status").toUpperCase()).append("\n");
                    
                    Date paymentDate = rs.getDate("payment_date");
                    if (paymentDate != null) {
                        details.append("💳 Payment Date: ").append(paymentDate).append("\n");
                    }
                    
                    JTextArea textArea = new JTextArea(details.toString());
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(400, 350));
                    
                    JOptionPane.showMessageDialog(this, scrollPane, "Fine Details", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading fine details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewFines());
    }
}

