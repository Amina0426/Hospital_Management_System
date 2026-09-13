package com.hms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/hms";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv("HMS_DB_PASSWORD");

    // Method to get connection
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to database!");
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        if (DBConnection.getConnection() != null) {
            System.out.println("✅ Connected to MySQL successfully!");
        } else {
            System.out.println("❌ Connection failed.");
        }
    }

}
