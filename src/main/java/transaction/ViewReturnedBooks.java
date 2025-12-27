package transaction;

import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewReturnedBooks extends JFrame {
    private JTable returnedTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JComboBox<String> filterCombo;
    
    private final String[] columnNames = {"Return ID", "Book Title", "Member Name", "Issue Date", "Return Date", "Days Borrowed", "Fine Amount"};

    public ViewReturnedBooks() {
        setTitle("Returned Books - Library Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createFilterPanel();
        createTablePanel();
        createFooter();
        
        loadReturnedBooks("all");
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 135, 84));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Returned Books");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Complete history of returned books and fines");
        subtitleLabel.setForeground(new Color(200, 255, 220));
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
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterPanel.add(filterLabel);
        
        String[] filterOptions = {"All Returns", "With Fines Only", "No Fines"};
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterCombo.setPreferredSize(new Dimension(160, 30));
        filterCombo.addActionListener(e -> applyFilter());
        filterPanel.add(filterCombo);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBackground(new Color(25, 135, 84));
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
        
        returnedTable = new JTable(tableModel);
        returnedTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        returnedTable.setRowHeight(28);
        returnedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        returnedTable.setGridColor(new Color(222, 226, 230));
        returnedTable.setShowGrid(true);
        
        JTableHeader header = returnedTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(25, 135, 84));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        returnedTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        returnedTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        returnedTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        returnedTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        returnedTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        returnedTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        returnedTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(returnedTable);
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
        
        statusLabel = new JLabel("0 books returned");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(108, 117, 125));
        footerPanel.add(statusLabel, BorderLayout.WEST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        viewDetailsButton.setForeground(Color.WHITE);
        viewDetailsButton.setBackground(new Color(13, 110, 253));
        viewDetailsButton.setFocusPainted(false);
        viewDetailsButton.setBorderPainted(false);
        viewDetailsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewDetailsButton.addActionListener(e -> viewReturnDetails());
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
        String selectedFilter = (String) filterCombo.getSelectedItem();
        if ("All Returns".equals(selectedFilter)) {
            loadReturnedBooks("all");
        } else if ("With Fines Only".equals(selectedFilter)) {
            loadReturnedBooks("with_fines");
        } else if ("No Fines".equals(selectedFilter)) {
            loadReturnedBooks("no_fines");
        }
    }

    private void loadReturnedBooks(String filter) {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> books = new ArrayList<>();
                
                StringBuilder query = new StringBuilder();
                query.append("SELECT rb.id, b.title, CONCAT(m.first_name, ' ', m.last_name) AS member_name, ");
                query.append("ib.issue_date, rb.return_date, rb.fine_amount ");
                query.append("FROM returned_books rb ");
                query.append("JOIN books b ON rb.book_id = b.id ");
                query.append("JOIN members m ON rb.member_id = m.id ");
                query.append("JOIN issued_books ib ON rb.issue_id = ib.id ");
                query.append("WHERE 1=1 ");
                
                if ("with_fines".equals(filter)) {
                    query.append("AND rb.fine_amount > 0 ");
                } else if ("no_fines".equals(filter)) {
                    query.append("AND rb.fine_amount = 0 ");
                }
                
                query.append("ORDER BY rb.return_date DESC");
                
                try (Connection conn = DBConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query.toString())) {
                    
                    while (rs.next()) {
                        int returnId = rs.getInt("id");
                        String bookTitle = rs.getString("title");
                        String memberName = rs.getString("member_name");
                        Date issueDate = rs.getDate("issue_date");
                        Date returnDate = rs.getDate("return_date");
                        double fineAmount = rs.getDouble("fine_amount");
                        
                        long daysBorrowed = java.time.temporal.ChronoUnit.DAYS.between(
                                issueDate.toLocalDate(), 
                                returnDate.toLocalDate()
                        );
                        
                        String fineStr = fineAmount > 0 ? "₹" + String.format("%.2f", fineAmount) : "₹0.00";
                        
                        books.add(new Object[]{
                                returnId,
                                bookTitle,
                                memberName,
                                issueDate.toString(),
                                returnDate.toString(),
                                daysBorrowed + " days",
                                fineStr
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return books;
            }
            
            @Override
            protected void done() {
                try {
                    List<Object[]> books = get();
                    tableModel.setRowCount(0);
                    for (Object[] book : books) {
                        tableModel.addRow(book);
                    }
                    
                    double totalFines = 0;
                    for (Object[] book : books) {
                        String fineStr = book[6].toString().replace("₹", "");
                        totalFines += Double.parseDouble(fineStr);
                    }
                    
                    statusLabel.setText(books.size() + " book(s) returned | Total fines: ₹" + String.format("%.2f", totalFines));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void viewReturnDetails() {
        int selectedRow = returnedTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a return record to view details", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int returnId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "SELECT rb.*, b.title, b.isbn, CONCAT(m.first_name, ' ', m.last_name) AS member_name, " +
                     "m.email, ib.issue_date, ib.due_date " +
                     "FROM returned_books rb " +
                     "JOIN books b ON rb.book_id = b.id " +
                     "JOIN members m ON rb.member_id = m.id " +
                     "JOIN issued_books ib ON rb.issue_id = ib.id " +
                     "WHERE rb.id = ?")) {
            pstmt.setInt(1, returnId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    StringBuilder details = new StringBuilder();
                    details.append("═══════════════════════════════════════\n");
                    details.append("        RETURN RECORD DETAILS\n");
                    details.append("═══════════════════════════════════════\n\n");
                    details.append("📝 Return ID: ").append(rs.getInt("id")).append("\n");
                    details.append("📖 Book Title: ").append(rs.getString("title")).append("\n");
                    details.append("🔖 ISBN: ").append(rs.getString("isbn")).append("\n");
                    details.append("👤 Member: ").append(rs.getString("member_name")).append("\n");
                    details.append("📧 Email: ").append(rs.getString("email")).append("\n\n");
                    details.append("📅 Issue Date: ").append(rs.getDate("issue_date")).append("\n");
                    details.append("📅 Due Date: ").append(rs.getDate("due_date")).append("\n");
                    details.append("📅 Return Date: ").append(rs.getDate("return_date")).append("\n\n");
                    
                    long daysBorrowed = java.time.temporal.ChronoUnit.DAYS.between(
                            rs.getDate("issue_date").toLocalDate(),
                            rs.getDate("return_date").toLocalDate()
                    );
                    details.append("⏱️ Days Borrowed: ").append(daysBorrowed).append(" days\n");
                    
                    double fine = rs.getDouble("fine_amount");
                    if (fine > 0) {
                        details.append("💰 Fine Amount: ₹").append(String.format("%.2f", fine)).append("\n");
                    } else {
                        details.append("✅ No Fine (Returned On Time)\n");
                    }
                    
                    JTextArea textArea = new JTextArea(details.toString());
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(420, 350));
                    
                    JOptionPane.showMessageDialog(this, scrollPane, "Return Details", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading return details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewReturnedBooks());
    }
}
