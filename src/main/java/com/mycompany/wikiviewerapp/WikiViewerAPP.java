/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp;

import com.mycompany.wikiviewerapp.db.DatabaseCreator;
import com.mycompany.wikiviewerapp.gui.gui_0;
import com.mycompany.wikiviewerapp.gui.Jgui_0;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
/**
 *
 * @author andri
 */
public class WikiViewerAPP {
    
    public static void main(String[] args) {        
            // 1) Initialize DB (not on the Swing UI thread)
        //for the actual run, it will happen like this
        try {
            DatabaseCreator.initializeDatabase();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Database initialization failed:\n" + ex.getMessage(),
                    "WikiViewerAPP",
                    JOptionPane.ERROR_MESSAGE);
            return; // don't start the GUI if DB isn't ready
        }

        // 2) Start GUI on the EDT
        SwingUtilities.invokeLater(() -> {
            new Jgui_0().setVisible(true);
        });

        }

}
    

