package wikiviewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
import javax.swing.JOptionPane;

public class DbRoutine {

    private static final String DB_PATH = "./wikiviewrdb"; //Η βάση δημιουργείται μέσα στον ενεργό φάκελο

    public static void createDatabaseAndTable() {
        String dbUrl = "jdbc:derby:" + DB_PATH + ";create=true";

        try (Connection conn = DriverManager.getConnection(dbUrl)) {

            JOptionPane.showMessageDialog(null, "Η Σύνδεση ήταν επιτυχής"); // Βγάζουμε ένα μύνημα σε mBox για να δούμε αν δημιουργήθηκε η βάση δεδομένων και ήταν επιτυχής η σύνδεση

            // Έλεγχος αν ο πίνακας υπάρχει ήδη
            DatabaseMetaData meta = conn.getMetaData();
            try (ResultSet rs = meta.getTables(null, "APP", "ARTICLEFROMAPI", null)) { // schema APP είναι το default
                if (rs.next()) {
                    //System.out.println("Ο πίνακας " + TABLE_NAME + " υπάρχει ήδη!");
                    JOptionPane.showMessageDialog(null, "Ο πίνακας ArticleFromAPI υπάρχει ήδη!");
                } else {
                    // Δημιουργία πίνακα αφού δεν υπάρχει
                    try (Statement st = conn.createStatement()) {
                        st.executeUpdate(
                                "CREATE TABLE " + "ArticleFromAPI" + " ("
                                + "PAGEID INT PRIMARY KEY, "
                                + "SNIPPET VARCHAR(1000),"
                                + "TITLE VARCHAR(50),"
                                + "ARTICLE CLOB,"
                                + "RATING INT,"
                                + "CATEGORY VARCHAR(30),"
                                + "COMMENTS VARCHAR(400))"
                        );
                        //System.out.println("Ο πίνακας " + TABLE_NAME + " δεν υπήρχε και δημιουργήθηκε.");
                        JOptionPane.showMessageDialog(null, "Ο πίνακας ArticleFromAPI δεν υπήρχε και δημιουργήθηκε.");
                    }
                }
            }

            try (ResultSet rs = meta.getTables(null, "APP", "CATEGORY", null)) { // schema APP είναι το default
                if (rs.next()) {
                    //System.out.println("Ο πίνακας " + TABLE_NAME + " υπάρχει ήδη!");
                    JOptionPane.showMessageDialog(null, "Ο πίνακας CATEGORY υπάρχει ήδη!");
                } else {
                    // Δημιουργία πίνακα αφού δεν υπάρχει
                    try (Statement st = conn.createStatement()) {
                        st.executeUpdate(
                                "CREATE TABLE " + "CATEGORY" + " ("
                                + "ID INT PRIMARY KEY, "
                                + "CATEGORY VARCHAR(30))"
                        );
                        //System.out.println("Ο πίνακας " + TABLE_NAME + " δεν υπήρχε και δημιουργήθηκε.");
                        JOptionPane.showMessageDialog(null, "Ο πίνακας CATEGORY δεν υπήρχε και δημιουργήθηκε.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
