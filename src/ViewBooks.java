import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewBooks extends JFrame {

    private JTable bookTable;
    private JScrollPane scrollPane;

    public ViewBooks() {
        setTitle("View All Books");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        // Table setup
        bookTable = new JTable();
        scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(20, 20, 550, 300);
        add(scrollPane);

        loadBookData();

        setVisible(true);
    }

    private void loadBookData() {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Title", "Author", "Quantity"});

        String sql = "SELECT * FROM books";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                model.addRow(new Object[]{id, title, author, quantity});
            }

            bookTable.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewBooks();
    }
}
