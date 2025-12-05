import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class SearchBook extends JFrame
{
    private JTextField idField, titleField;
    private JButton searchButton;

    public SearchBook()
    {
        setTitle("Search Book");
        setSize(400, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Book ID:");
        idLabel.setBounds(30, 30, 100, 30);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(130, 30, 200, 30);
        add(idField);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setBounds(30, 70, 100, 30);
        add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(130, 70, 200, 30);
        add(titleField);

        searchButton = new JButton("Search");
        searchButton.setBounds(130, 120, 100, 30);
        add(searchButton);

        searchButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                searchBook();
            }
        });

        setVisible(true);
    }

    private void searchBook()
    {
        String idText = idField.getText().trim();
        String titleText = titleField.getText().trim();

        if(idText.isEmpty() && titleText.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Enter ID or Title to search!");
            return;
        }

        String sql = "SELECT * FROM books WHERE id = ? OR title = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            pstmt.setString(1, idText);
            pstmt.setString(2, titleText);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next())
            {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int quantity = rs.getInt("quantity");

                JOptionPane.showMessageDialog(this,
                        "📘 Book Found:\nID: " + id + "\nTitle: " + title +
                                "\nAuthor: " + author + "\nQuantity: " + quantity);
            }
            else
            {
                JOptionPane.showMessageDialog(this, "❌ Book not found.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        new SearchBook();
    }
}
