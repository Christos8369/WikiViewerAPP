/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andri
 */
public class DatabaseCreator {

    /**
     * JDBC URL for connecting to the embedded Apache Derby database used by WikiViewerAPP.
     * The 'create=true' parameter ensures the database is created locally
     * on the user's machine the first time the application is run.
     */
    private static final String DB_URL =
            "jdbc:derby:WikiViewerAPP_DB;create=true";
    


    public static void initializeDatabase() {
 // Set the system directory for storing the Derby database files.
        System.setProperty("derby.system.home", "C:/derby");
        Connection conn = null;
        Statement stmt = null;
        try {
        // Load the Apache Derby embedded database driver.
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Derby Driver Loaded Successfully.");
             // Check if the database already exists.
            if (databaseExists()) {
                System.out.println("Database already exists. Connecting...");
                conn = DriverManager.getConnection(DB_URL);
            } else {
                System.out.println("Database not found. Creating a new database...");
                conn = DriverManager.getConnection(DB_URL);
                stmt = conn.createStatement();
                createTables(stmt);
                System.out.println("Database Created Successfully.");
            }
            } catch (ClassNotFoundException e) {
                System.out.println("Derby Driver Not Found. Ensure that derby.jar is in the classpath.");
                e.printStackTrace();
                } catch (SQLException e) {
                    System.out.println("Database Error encountered.");
                    e.printStackTrace();
                } finally {
                // Close the Statement and Connection objects to free up resources.
                    try {
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                        } catch (SQLException e) {
                        System.out.println("Failed to close database connection.");
                        e.printStackTrace();
                        }
                    }
                } 

    
     /**
 * Checks if the Derby database exists by attempting to establish a connection.
 *
 * @return true if the database exists, false otherwise.
 */
    private static boolean databaseExists() {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:universityDB")) {
         // If the connection is successful, the database exists.
        return true;
        } catch (SQLException e) {
        // If the connection fails, the database does not exist.
        return false;
         }
    }

    
}
