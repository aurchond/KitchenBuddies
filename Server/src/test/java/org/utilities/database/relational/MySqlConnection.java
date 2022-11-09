package org.utilities.database.relational;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySqlConnection {
    private static final String url = "jdbc:mysql://localhost";
    private static final String user = "shadi";
    private static final String password = "password";
    public static void main(String args[]) {
        try (Connection con = DriverManager.getConnection(url, user, password);
            Statement stmt = con.createStatement();
        ) {
            String sql = "CREATE DATABASE BUDDIES";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully!"); 
        } catch (Exception e) {
            e.printStackTrace();
   } 
    }
}
