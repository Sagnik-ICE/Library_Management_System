import java.sql.*;

public class ShowPasswords {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Sagnik2314";
        
        System.out.println("\n=== STORED PASSWORDS IN DATABASE ===\n");
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, password FROM users ORDER BY username")) {
            
            while (rs.next()) {
                String username = rs.getString("username");
                String pass = rs.getString("password");
                
                System.out.println("Username: " + username);
                System.out.println("Password: " + pass);
                System.out.println("Length: " + pass.length());
                System.out.println("Starts with $2: " + pass.startsWith("$2"));
                System.out.println();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
