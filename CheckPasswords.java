import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class CheckPasswords {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Sagnik2314";
        
        System.out.println("\n=== PASSWORD CHECK ===\n");
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT username, password, role FROM users")) {
            
            while (rs.next()) {
                String username = rs.getString("username");
                String storedPass = rs.getString("password");
                String role = rs.getString("role");
                
                System.out.println("User: " + username + " (" + role + ")");
                System.out.println("  Stored password: " + (storedPass.length() > 20 ? storedPass.substring(0, 20) + "..." : storedPass));
                
                // Check if it's BCrypt (starts with $2a$ or $2b$)
                if (storedPass.startsWith("$2")) {
                    System.out.println("  Type: BCrypt Hash ✓");
                    
                    // Test the expected password
                    String testPass = username + "123";
                    if (username.equals("admin")) testPass = "admin123";
                    
                    boolean matches = BCrypt.checkpw(testPass, storedPass);
                    System.out.println("  Testing '" + testPass + "': " + (matches ? "✓ CORRECT" : "✗ WRONG"));
                } else {
                    System.out.println("  Type: Plain Text");
                    System.out.println("  Value: " + storedPass);
                }
                System.out.println();
            }
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
