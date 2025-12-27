package transaction;

import database.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ViewIssuedBooks extends JFrame {
    private JTable issuedTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JComboBox<String> filterCombo;
    
    private final String[] columnNames = {"Issue ID", "Book Title", "Member Name", "Issue Date", "Due Date", "Days Remaining", "Status"};

    public ViewIssuedBooks() {
        setTitle("Issued Books - Library Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createFilterPanel();
        createTablePanel();
        createFooter();
        
        loadIssuedBooks("all");
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 193, 7));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Issued Books");
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Track all currently issued books and due dates");
        subtitleLabel.setForeground(new Color(102, 102, 102));
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
        
        String[] filterOptions = {"All Books", "Due Soon (3 days)", "Overdue Only"};
        filterCombo = new JComboBox<>(filterOptions);
        filterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterCombo.setPreferredSize(new Dimension(160, 30));
        filterCombo.addActionListener(e -> applyFilter());
        filterPanel.add(filterCombo);
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshButton.setForeground(new Color(51, 51, 51));
        refreshButton.setBackground(new Color(255, 193, 7));
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
        
        issuedTable = new JTable(tableModel);
        issuedTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        issuedTable.setRowHeight(28);
        issuedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        issuedTable.setGridColor(new Color(222, 226, 230));
        issuedTable.setShowGrid(true);
        
        JTableHeader header = issuedTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(255, 193, 7));
        header.setForeground(new Color(51, 51, 51));
        header.setReorderingAllowed(false);
        
        issuedTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        issuedTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        issuedTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        issuedTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        issuedTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        issuedTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        issuedTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        // Custom renderer for status coloring
        DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String status = table.getValueAt(row, 6).toString();
                    if ("OVERDUE".equals(status)) {
                        c.setBackground(new Color(255, 230, 230));
                        c.setForeground(new Color(220, 53, 69));
                        setFont(new Font("Segoe UI", Font.BOLD, 11));
                    } else if ("DUE SOON".equals(status)) {
                        c.setBackground(new Color(255, 243, 205));
                        c.setForeground(new Color(255, 140, 0));
                        setFont(new Font("Segoe UI", Font.BOLD, 11));
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                        setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    }
                }
                return c;
            }
        };
        
        for (int i = 0; i < issuedTable.getColumnCount(); i++) {
            issuedTable.getColumnModel().getColumn(i).setCellRenderer(statusRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(issuedTable);
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
        
        statusLabel = new JLabel("0 books issued");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(108, 117, 125));
        footerPanel.add(statusLabel, BorderLayout.WEST);
        
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        legendPanel.setOpaque(false);
        
        JLabel normalLabel = new JLabel("● Normal");
        normalLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        normalLabel.setForeground(new Color(25, 135, 84));
        legendPanel.add(normalLabel);
        
        JLabel dueSoonLabel = new JLabel("● Due Soon");
        dueSoonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        dueSoonLabel.setForeground(new Color(255, 140, 0));
        legendPanel.add(dueSoonLabel);
        
        JLabel overdueLabel = new JLabel("● Overdue");
        overdueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        overdueLabel.setForeground(new Color(220, 53, 69));
        legendPanel.add(overdueLabel);
        
        footerPanel.add(legendPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
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
        if ("All Books".equals(selectedFilter)) {
            loadIssuedBooks("all");
        } else if ("Due Soon (3 days)".equals(selectedFilter)) {
            loadIssuedBooks("due_soon");
        } else if ("Overdue Only".equals(selectedFilter)) {
            loadIssuedBooks("overdue");
        }
    }

    private void loadIssuedBooks(String filter) {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> books = new ArrayList<>();
                
                String query = "SELECT ib.id, b.title, CONCAT(m.first_name, ' ', m.last_name) AS member_name, " +
                        "ib.issue_date, ib.due_date " +
                        "FROM issued_books ib " +
                        "JOIN books b ON ib.book_id = b.id " +
                        "JOIN members m ON ib.member_id = m.id " +
                        "WHERE ib.return_date IS NULL " +
                        "ORDER BY ib.due_date ASC";
                
                try (Connection conn = DBConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    
                    LocalDate today = LocalDate.now();
                    
                    while (rs.next()) {
                        int issueId = rs.getInt("id");
                        String bookTitle = rs.getString("title");
                        String memberName = rs.getString("member_name");
                        Date issueDate = rs.getDate("issue_date");
                        Date dueDate = rs.getDate("due_date");
                        
                        LocalDate due = dueDate.toLocalDate();
                        long daysRemaining = ChronoUnit.DAYS.between(today, due);
                        
                        String status;
                        String daysRemainingStr;
                        
                        if (daysRemaining < 0) {
                            status = "OVERDUE";
                            daysRemainingStr = Math.abs(daysRemaining) + " days late";
                        } else if (daysRemaining <= 3) {
                            status = "DUE SOON";
                            daysRemainingStr = daysRemaining + " days";
                        } else {
                            status = "NORMAL";
                            daysRemainingStr = daysRemaining + " days";
                        }
                        
                        // Apply filter
                        boolean include = false;
                        if ("all".equals(filter)) {
                            include = true;
                        } else if ("overdue".equals(filter) && "OVERDUE".equals(status)) {
                            include = true;
                        } else if ("due_soon".equals(filter) && ("DUE SOON".equals(status) || "OVERDUE".equals(status))) {
                            include = true;
                        }
                        
                        if (include) {
                            books.add(new Object[]{
                                    issueId,
                                    bookTitle,
                                    memberName,
                                    issueDate.toString(),
                                    dueDate.toString(),
                                    daysRemainingStr,
                                    status
                            });
                        }
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
                    
                    long overdueCount = books.stream().filter(b -> "OVERDUE".equals(b[6])).count();
                    long dueSoonCount = books.stream().filter(b -> "DUE SOON".equals(b[6])).count();
                    
                    statusLabel.setText(books.size() + " book(s) issued | " + 
                            overdueCount + " overdue | " + 
                            dueSoonCount + " due soon");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewIssuedBooks());
    }
}
