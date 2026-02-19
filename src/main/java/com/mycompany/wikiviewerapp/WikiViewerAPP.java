/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp;

import com.mycompany.wikiviewerapp.db.DatabaseCreator;
import com.mycompany.wikiviewerapp.gui.Jgui_0;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
/**
 *
 * @author andri
 */
public class WikiViewerAPP {
    
    public static void main(String[] args) {        
            // 1) Initialize DB (not on the Swing UI thread)
        //for the actual run, it will happen like this
        System.out.println("MAIN STARTED");

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("com.mycompany_WikiViewerAPP_jar_1.0-SNAPSHOTPU");

        System.out.println("EMF OPEN: " + emf.isOpen());
        emf.close();

        System.out.println("MAIN DONE");

        // 2) Start GUI on the EDT
        SwingUtilities.invokeLater(() -> {
            new Jgui_0().setVisible(true);
        });

        }

}


