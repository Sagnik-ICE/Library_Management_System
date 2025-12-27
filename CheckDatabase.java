import java.sql.*;

public class CheckDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Sagnik2314";
        
        System.out.println("\n=== DATABASE CONNECTION CHECK ===\n");
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✓ Connected to: " + url);
            System.out.println("✓ Database: library_db\n");
            
            // Check users table
            System.out.println("--- USERS TABLE ---");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users")) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.println("Total Users: " + count);
                    
                    if (count == 0) {
                        System.out.println("\n⚠ WARNING: Users table is EMPTY!");
                        System.out.println("   You need to run the SQL files:");
                        System.out.println("   1. src\\main\\resources\\db\\library_db.sql");
                        System.out.println("   2. src\\main\\resources\\db\\test_db.sql");
                    } else {
                        // Show users
                        System.out.println("\nExisting Users:");
                        try (ResultSet rs2 = stmt.executeQuery("SELECT username, role, status FROM users")) {
                            while (rs2.next()) {
                                System.out.printf("  - %s (%s) - Status: %s\n", 
                                    rs2.getString("username"),
                                    rs2.getString("role"),
                                    rs2.getString("status"));
                            }
                        }
                    }
                }
            }
            
            // Check books table
            System.out.println("\n--- BOOKS TABLE ---");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM books")) {
                if (rs.next()) {
                    System.out.println("Total Books: " + rs.getInt("count"));
                }
            }
            
            // Check members table
            System.out.println("\n--- MEMBERS TABLE ---");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM members")) {
                if (rs.next()) {
                    System.out.println("Total Members: " + rs.getInt("count"));
                }
            }
            
            System.out.println("\n=== CHECK COMPLETE ===\n");
            
        } catch (SQLException e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            if (e.getMessage().contains("doesn't exist")) {
                System.err.println("\n  Database 'library_db' doesn't exist or tables not created!");
                System.err.println("  Run library_db.sql first to create the schema.");
            }
        }
    }
}
