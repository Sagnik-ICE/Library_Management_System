import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ViewMembers extends JFrame {
    JTable table;

    public ViewMembers() {
        setTitle("All Registered Members");
        setSize(600, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Member Records");
        label.setBounds(20, 10, 200, 30);
        add(label);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 50, 540, 280);
        add(scrollPane);

        loadMembers();

        setVisible(true);
    }

    private void loadMembers() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Email");
        model.addColumn("Contact");

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM members")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("contact")
                });
            }

            table.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading members: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ViewMembers();
    }
}
