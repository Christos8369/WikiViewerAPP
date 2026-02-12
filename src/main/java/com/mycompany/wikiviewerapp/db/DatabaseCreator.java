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
    

    
    /**
     * Initializes the database by setting the Derby system home directory,
     * loading the database driver, checking if the database already exists,
     * and creating the required tables if necessary.
     */
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
        try (Connection conn = DriverManager.getConnection("jdbc:derby:WikiViewerAPP_DB")) {
         // If the connection is successful, the database exists.
        return true;
        } catch (SQLException e) {
        // If the connection fails, the database does not exist.
        return false;
         }
    }
    
    
        /**
     * Creates the necessary tables in the database, including COUNTRY and UNIVERSITY.
     *
     * @param stmt The Statement object used to execute SQL queries.
     * @throws SQLException if an error occurs during table creation.
     * if need, we have to run an alter table SQL command if we want to change it
     * after the first run of the application ( adding a column for example )
     */
         private static void createTables(Statement stmt) throws SQLException {
        // SQL statement to create the COMMENT table, which holds bellow attribues.
        String createCommentTable = """
        CREATE TABLE COMMENT (
            COMMENT_ID INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            ARTICLE_PAGE_ID INTEGER NOT NULL,
            TEXT_OF_COMMENT VARCHAR(255) NOT NULL,
            CONSTRAINT FK_COMMENT_ARTICLE
                FOREIGN KEY (ARTICLE_PAGE_ID)
                REFERENCES ARTICLE (PAGEID)
        )
        """;

        
         // SQL statement to create the ARTICLE table, which holds bellow attributes.
        String createArticleTable = """
        CREATE TABLE ARTICLE (
            PAGEID       INTEGER PRIMARY KEY,
            TITLE        VARCHAR(255) NOT NULL,
            SIZE         INTEGER,
            CATEGORY VARCHAR(255) DEFAULT 'Uncategorized' NOT NULL,
            RATING INTEGER CHECK (RATING BETWEEN 0 AND 5),
            SNIPPET      VARCHAR(255),
            TEXT         CLOB,
            WORD_COUNT   INTEGER
        )
        """;
        stmt.executeUpdate(createArticleTable);
        System.out.println("ARTICLE table created.");
        
        stmt.executeUpdate(createCommentTable);
        System.out.println("COMMENT Table Created.");
        }


        /**
        * Clears all rows from the two tables
        * The method first deletes rows from the COMMENT table
        * and then deletes rows from the COUNTRY table.
        */
            public static void clearAllRows() throws SQLException {
                System.setProperty("derby.system.home", "C:/derby");

                try {
                    Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
                } catch (ClassNotFoundException e) {
                    throw new SQLException("Derby driver not found", e);
                }

                try (Connection conn = DriverManager.getConnection(DB_URL);
                     Statement stmt = conn.createStatement()) {

                    // child first, then parent
                    stmt.executeUpdate("DELETE FROM COMMENT");
                    stmt.executeUpdate("DELETE FROM ARTICLE");

                    System.out.println("All rows deleted from COMMENT and ARTICLE.");
                }
            }

    }

    

          

