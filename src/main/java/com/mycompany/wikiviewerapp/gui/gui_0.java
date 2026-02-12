/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp.gui;

import javax.swing.*;
import java.awt.*;

public class gui_0 extends JFrame {

    public gui_0() {
        setTitle("WikiViewer - Menu");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainUI = new JPanel(new BorderLayout(10, 10));

        JLabel title = new JLabel("Choose an option:");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainUI.add(title, BorderLayout.NORTH);

        JPanel options = new JPanel(new GridLayout(9, 1, 10, 10));

        JButton opt1 = new JButton("1. Αναζήτηση άρθρου (Live από το API)");
        options.add(opt1);
        opt1.setEnabled(true);
        
        JButton opt2 = new JButton("2. Ενημέρωση πληροφοριών άρθρου (στην τοπική ΒΔ)");
        options.add(opt2);
        opt2.setEnabled(true);


        JButton opt3 = new JButton (" 3. Προσθήκη πληροφοριών για άρθρο (π.χ. σχόλια, βαθμολογία");
        options.add(opt3);
        opt3.setEnabled(true);
        
      
        JButton opt4 = new JButton (" 4. Προβολή αποθηκευμένων άρθρων ανά κατηγορία ");
        options.add(opt4);
        opt4.setEnabled(true);
        
        JButton opt5  = new JButton (" 5. Αναζήτηση άρθρου βάσει keyword (σε τίτλο ή κείμενο) ");        
        options.add(opt5);
        opt5.setEnabled(true);
        
        JButton opt6  = new JButton (" 6. Προβολή στατιστικών ");        
        options.add(opt6);
        opt6.setEnabled(true);
        
        JButton opt7  = new JButton (" 7. Προσθήκη νέου άρθρου σε κατηγορία (Χειροκίνητη εισαγωγή)");      
        options.add(opt7);
        opt7.setEnabled(false);
        
        JButton opt8  = new JButton (" 8. Τροποποίηση άρθρων σε κατηγορία ");        
        options.add(opt8);
        opt8.setEnabled(false);

        JButton opt9  = new JButton ("9. Προσθήκη κατηγορίας");        
        options.add(opt9);
        opt9.setEnabled(false);


        mainUI.add(options, BorderLayout.CENTER);
        setContentPane(mainUI);

        // When option 1 is clicked -> open GUI1_1
        opt1.addActionListener(e -> {
            new GUI1_1();   // invoke constructor of the other GUI
            dispose();      // close this window (or use setVisible(false))
        });

        setVisible(true);
    
    }
}