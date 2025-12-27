package book;

import database.DBConnection;
import utils.Constants;
import models.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViewBooks extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(ViewBooks.class);
    
    private JTable booksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JLabel statusLabel;
    private List<Book> allBooks;

    public ViewBooks() {
        setTitle("View Books - Library Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        createHeader();
        createSearchPanel();
        createTablePanel();
        createFooter();

        loadBooks();
        setVisible(true);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Constants.HEADER_BLUE);
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("All Books");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        JLabel subtitleLabel = new JLabel("View and manage your book collection");
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
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        searchPanel.setBackground(new Color(248, 249, 250));

        // Left: Search
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        
        JLabel searchLabel = new JLabel("🔍 Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        leftPanel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterBooks();
            }
        });
        leftPanel.add(searchField);

        // Middle: Filter
        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        midPanel.setOpaque(false);
        
        JLabel filterLabel = new JLabel("📋 Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        midPanel.add(filterLabel);
        
        String[] filters = {"All", "Title", "Author", "Genre"};
        filterComboBox = new JComboBox<>(filters);
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filterComboBox.addActionListener(e -> filterBooks());
        midPanel.add(filterComboBox);

        // Right: Refresh
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setBackground(Constants.HEADER_BLUE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorderPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            loadBooks();
            filterBooks();
        });
        rightPanel.add(refreshBtn);

        searchPanel.add(leftPanel, BorderLayout.WEST);
        searchPanel.add(midPanel, BorderLayout.CENTER);
        searchPanel.add(rightPanel, BorderLayout.EAST);
        
        add(searchPanel, BorderLayout.NORTH);
    }

    private void createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        String[] columnNames = {"ID", "Unique ID", "ISBN", "Title", "Author", "Genre", "Publisher", "Price", "Quantity", "Received Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        booksTable = new JTable(tableModel);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        booksTable.setRowHeight(25);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setShowGrid(true);
        booksTable.setGridColor(new Color(220, 220, 220));
        
        // Header styling
        JTableHeader header = booksTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(Constants.HEADER_BLUE);
        header.setForeground(Color.WHITE);
        
        // Column widths
        int[] widths = {50, 100, 100, 150, 150, 100, 100, 80, 80, 100};
        for (int i = 0; i < widths.length; i++) {
            booksTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(booksTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }

    private void createFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(248, 249, 250));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        statusLabel = new JLabel("Loading books...");
        statusLabel.setForeground(new Color(108, 117, 125));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerPanel.add(statusLabel, BorderLayout.WEST);

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBackground(new Color(108, 117, 125));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(closeBtn);
        footerPanel.add(rightPanel, BorderLayout.EAST);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadBooks() {
        SwingWorker<List<Book>, Void> worker = new SwingWorker<List<Book>, Void>() {
            @Override
            protected List<Book> doInBackground() throws Exception {
                List<Book> books = new ArrayList<>();
                try (Connection conn = DBConnection.getConnection();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(
                             "SELECT id, unique_book_id, isbn, title, author, genre, publisher, price, quantity, date_received FROM books ORDER BY title")) {
                    
                    while (rs.next()) {
                        Book book = new Book();
                        book.setId(rs.getInt("id"));
                        book.setUniqueBookId(rs.getString("unique_book_id"));
                        book.setIsbn(rs.getString("isbn"));
                        book.setTitle(rs.getString("title"));
                        book.setAuthor(rs.getString("author"));
                        book.setGenre(rs.getString("genre"));
                        book.setPublisher(rs.getString("publisher"));
                        book.setPrice(rs.getDouble("price"));
                        book.setQuantity(rs.getInt("quantity"));
                        book.setDateReceived(rs.getDate("date_received"));
                        books.add(book);
                    }
                } catch (SQLException e) {
                    logger.error("Error loading books", e);
                }
                return books;
            }

            @Override
            protected void done() {
                try {
                    allBooks = get();
                    displayBooks(allBooks);
                    statusLabel.setText("Total books: " + allBooks.size());
                } catch (Exception e) {
                    logger.error("Error in background worker", e);
                }
            }
        };
        worker.execute();
    }

    private void displayBooks(List<Book> books) {
        tableModel.setRowCount(0);
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getId(),
                book.getUniqueBookId(),
                book.getIsbn() != null ? book.getIsbn() : "-",
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPublisher() != null ? book.getPublisher() : "-",
                String.format("₹%.2f", book.getPrice()),
                book.getQuantity(),
                book.getDateReceived() != null ? book.getDateReceived().toString() : "-"
            });
        }
    }

    private void filterBooks() {
        if (allBooks == null) return;
        
        String searchTerm = searchField.getText().toLowerCase().trim();
        String filterType = (String) filterComboBox.getSelectedItem();
        
        List<Book> filtered = new ArrayList<>();
        
        for (Book book : allBooks) {
            if (searchTerm.isEmpty()) {
                filtered.add(book);
            } else {
                boolean match = false;
                
                if (filterType.equals("All")) {
                    match = book.getTitle().toLowerCase().contains(searchTerm) ||
                           book.getAuthor().toLowerCase().contains(searchTerm) ||
                           book.getGenre().toLowerCase().contains(searchTerm) ||
                           (book.getUniqueBookId() != null && book.getUniqueBookId().toLowerCase().contains(searchTerm));
                } else if (filterType.equals("Title")) {
                    match = book.getTitle().toLowerCase().contains(searchTerm);
                } else if (filterType.equals("Author")) {
                    match = book.getAuthor().toLowerCase().contains(searchTerm);
                } else if (filterType.equals("Genre")) {
                    match = book.getGenre().toLowerCase().contains(searchTerm);
                }
                
                if (match) {
                    filtered.add(book);
                }
            }
        }
        
        displayBooks(filtered);
        statusLabel.setText("Showing " + filtered.size() + " of " + allBooks.size() + " books");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewBooks());
    }
}
