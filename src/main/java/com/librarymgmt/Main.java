package com.librarymgmt;

import java.sql.Connection;
import com.librarymgmt.config.DBConnection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✅ Connected to database successfully!");
        } else {
            System.out.println("❌ Failed to connect to database.");
        }
    }
}
