package book;

import database.DBConnection;
import utils.Constants;
import models.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchBook extends JFrame {
    private JTextField searchField;
    private JComboBox<String> searchCriteriaCombo, genreFilterCombo;
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    
    private final String[] searchCriteria = {"All", "Title", "Author", "ISBN", "Publisher", "Genre"};
    private final String[] columnNames = {"ID", "Unique ID", "ISBN", "Title", "Author", "Genre", "Publisher", "Price", "Qty", "Date"};

    public SearchBook() {
        setTitle("Search Books - Library Management System");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createSearchPanel();
        createTablePanel();
        createFooter();
        
        loadAllBooks();
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Constants.HEADER_BLUE);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("🔍 Search Books");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("Advanced book search with multiple filters");
        subtitleLabel.setForeground(new Color(220, 240, 255));
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

    private void createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        searchPanel.setBackground(new Color(248, 249, 250));
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 226, 230)));
        
        JLabel criteriaLabel = new JLabel("Search By:");
        criteriaLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchPanel.add(criteriaLabel);
        
        searchCriteriaCombo = new JComboBox<>(searchCriteria);
        searchCriteriaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchCriteriaCombo.setPreferredSize(new Dimension(120, 30));
        searchCriteriaCombo.addActionListener(e -> updateSearchPlaceholder());
        searchPanel.add(searchCriteriaCombo);
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setToolTipText("Enter search text...");
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField);
        
        JButton searchButton = new JButton("🔍 Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(Constants.HEADER_BLUE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.addActionListener(e -> performSearch());
        searchPanel.add(searchButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
        clearButton.setForeground(Color.WHITE);
        clearButton.setBackground(new Color(108, 117, 125));
        clearButton.setFocusPainted(false);
        clearButton.setBorderPainted(false);
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.addActionListener(e -> clearSearch());
        searchPanel.add(clearButton);
        
        searchPanel.add(new JSeparator(SwingConstants.VERTICAL));
        
        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchPanel.add(genreLabel);
        
        String[] genreOptions = new String[Constants.BOOK_GENRES.length + 1];
        genreOptions[0] = "All Genres";
        System.arraycopy(Constants.BOOK_GENRES, 0, genreOptions, 1, Constants.BOOK_GENRES.length);
        
        genreFilterCombo = new JComboBox<>(genreOptions);
        genreFilterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        genreFilterCombo.setPreferredSize(new Dimension(140, 30));
        genreFilterCombo.addActionListener(e -> performSearch());
        searchPanel.add(genreFilterCombo);
        
        add(searchPanel, BorderLayout.NORTH);
        
        JPanel combinedPanel = new JPanel(new BorderLayout());
        JPanel header = (JPanel) getContentPane().getComponent(0);
        combinedPanel.add(header, BorderLayout.NORTH);
        combinedPanel.add(searchPanel, BorderLayout.SOUTH);
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
        
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        resultsTable.setRowHeight(25);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsTable.setGridColor(new Color(222, 226, 230));
        resultsTable.setShowGrid(true);
        
        JTableHeader header = resultsTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Constants.HEADER_BLUE);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        
        resultsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        resultsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        resultsTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        resultsTable.getColumnModel().getColumn(3).setPreferredWidth(200);
        resultsTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        resultsTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        resultsTable.getColumnModel().getColumn(6).setPreferredWidth(120);
        resultsTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        resultsTable.getColumnModel().getColumn(8).setPreferredWidth(50);
        resultsTable.getColumnModel().getColumn(9).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
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
        
        statusLabel = new JLabel("0 books found");
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
        viewDetailsButton.addActionListener(e -> viewBookDetails());
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

    private void updateSearchPlaceholder() {
        String criteria = (String) searchCriteriaCombo.getSelectedItem();
        searchField.setToolTipText("Search by " + criteria.toLowerCase() + "...");
    }

    private void performSearch() {
        String searchText = searchField.getText().trim();
        String criteria = (String) searchCriteriaCombo.getSelectedItem();
        String genre = (String) genreFilterCombo.getSelectedItem();
        
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() {
                List<Object[]> books = new ArrayList<>();
                StringBuilder query = new StringBuilder("SELECT * FROM books WHERE 1=1");
                
                if (!searchText.isEmpty() && !"All".equals(criteria)) {
                    String column = getColumnName(criteria);
                    query.append(" AND ").append(column).append(" LIKE ?");
                }
                
                if (!"All Genres".equals(genre)) {
                    query.append(" AND genre = ?");
                }
                
                query.append(" ORDER BY title");
                
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
                    
                    int paramIndex = 1;
                    if (!searchText.isEmpty() && !"All".equals(criteria)) {
                        pstmt.setString(paramIndex++, "%" + searchText + "%");
                    }
                    if (!"All Genres".equals(genre)) {
                        pstmt.setString(paramIndex, genre);
                    }
                    
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            books.add(new Object[]{
                                    rs.getInt("id"),
                                    rs.getString("unique_book_id"),
                                    rs.getString("isbn"),
                                    rs.getString("title"),
                                    rs.getString("author"),
                                    rs.getString("genre"),
                                    rs.getString("publisher"),
                                    String.format("₹%.2f", rs.getDouble("price")),
                                    rs.getInt("quantity"),
                                    rs.getDate("date_received")
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
                    statusLabel.setText(books.size() + " book(s) found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private String getColumnName(String criteria) {
        switch (criteria) {
            case "Title":
                return "title";
            case "Author":
                return "author";
            case "ISBN":
                return "isbn";
            case "Publisher":
                return "publisher";
            case "Genre":
                return "genre";
            default:
                return "title";
        }
    }

    private void loadAllBooks() {
        searchCriteriaCombo.setSelectedItem("All");
        genreFilterCombo.setSelectedIndex(0);
        searchField.setText("");
        performSearch();
    }

    private void clearSearch() {
        searchField.setText("");
        searchCriteriaCombo.setSelectedItem("All");
        genreFilterCombo.setSelectedIndex(0);
        loadAllBooks();
    }

    private void viewBookDetails() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to view details", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE id = ?")) {
            pstmt.setInt(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    StringBuilder details = new StringBuilder();
                    details.append("═══════════════════════════════════════\n");
                    details.append("           BOOK DETAILS\n");
                    details.append("═══════════════════════════════════════\n\n");
                    details.append("📌 Book ID: ").append(rs.getInt("id")).append("\n");
                    details.append("📚 Unique ID: ").append(rs.getString("unique_book_id")).append("\n");
                    details.append("📖 ISBN: ").append(rs.getString("isbn")).append("\n");
                    details.append("📕 Title: ").append(rs.getString("title")).append("\n");
                    details.append("✍️ Author: ").append(rs.getString("author")).append("\n");
                    details.append("🏷️ Genre: ").append(rs.getString("genre")).append("\n");
                    details.append("🏢 Publisher: ").append(rs.getString("publisher")).append("\n");
                    details.append("💰 Price: ₹").append(String.format("%.2f", rs.getDouble("price"))).append("\n");
                    details.append("📊 Quantity: ").append(rs.getInt("quantity")).append("\n");
                    details.append("📅 Date Received: ").append(rs.getDate("date_received")).append("\n");
                    
                    String description = rs.getString("description");
                    if (description != null && !description.isEmpty()) {
                        details.append("\n📝 Description:\n").append(description).append("\n");
                    }
                    
                    JTextArea textArea = new JTextArea(details.toString());
                    textArea.setEditable(false);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(450, 350));
                    
                    JOptionPane.showMessageDialog(this, scrollPane, "Book Details", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading book details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchBook());
    }
}
