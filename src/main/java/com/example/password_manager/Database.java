package com.example.password_manager;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final String URL = "jdbc:sqlite:database.db";

    public Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }// end of connect

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS passwords (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usage TEXT NOT NULL, " +
                "password TEXT NOT NULL);";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }// end of createTable

    public void insertPassword(String usage, String password) {
        String sql = "INSERT INTO passwords(usage, password) VALUES(?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if(getPassword(usage) != null) {return;}
            pstmt.setString(1, usage);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("Password added: " + usage);
        } catch (SQLException e) {
            System.out.println("Insert error: " + e.getMessage());
        }
    }// end of insertPassword

    public String getPassword(String usage) {
        String sql = "SELECT password FROM passwords WHERE usage = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usage);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            System.out.println("Error getting password: " + e.getMessage());
        }
        return null; // Return null if no matching record is found
    }// end of getPassword

    public void printAllPasswords() {
        String sql = "SELECT * FROM passwords";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("=== Stored Passwords ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        " | Usage: " + rs.getString("usage") +
                        " | Password: " + rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("Error printing passwords: " + e.getMessage());
        }
    }// end of printAllPasswords

    public ArrayList<String> get_all_usages(){
        String sql = "SELECT * FROM passwords";
        ArrayList<String> output = new ArrayList<>();
        try(Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()) {
                output.add(rs.getString("usage"));
            }
        }catch (SQLException e) {
            System.out.println("Error getting all usages: " + e.getMessage());
        }
        return output;
    }//end of get_all_usages

    public void deletePassword(String usage) {
        String sql = "DELETE FROM passwords WHERE usage = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usage);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password deleted for usage: " + usage);
            } else {
                System.out.println("No password found for usage: " + usage);
            }
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
        }
    }// end of deletePassword
}// end of class
