/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.wikiviewerapp.gui;

import com.mycompany.wikiviewerapp.api.WikiApiClient;
import com.mycompany.wikiviewerapp.api.WikiSearchResult;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JList;
import com.mycompany.wikiviewerapp.db.ArticleDAO;
import com.mycompany.wikiviewerapp.model.Article;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.Dimension;
import java.awt.Image;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

/**
 *
 * @author andri
 */
public class Jgui1_1 extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Jgui1_1.class.getName());

    //private JList<WikiSearchResult> resultsList;
    private final java.util.Set<Integer> savedPageIds = new java.util.HashSet<>();

    // helper inside GUI class
    private static String shortSnippet(String s) {
        s = WikiApiClient.stripHtml(s);
        if (s == null) {
            return "";
        }
        s = s.replaceAll("\\s+", " ").trim();
        return (s.length() > 180) ? s.substring(0, 180) + "…" : s;
    }
    //Icon της σελίδας
    Image pageIcon = new ImageIcon(Jgui1_1.class.getResource("/wikilogo.png")).getImage();

    //Επειδή θα χρειαστούμε κάποια εικονίδια βάζω την διαμόρφωση εδώ για να ορίζω μετά στον constructor μόνο την εικόνα και να μην γράφω συνέχεια κώδικα για κάθε εικονίδιο
    private ImageIcon scaledIcon(String path, int w, int h) {
        Image img = new ImageIcon(Jgui1_1.class.getResource(path)).getImage();
        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
    //Επειδή χρειάζομαι αυτή η φόρμα να ανοίγει και μετά να γίνεται dispose χωρίς να χάνω την Gui_0 βάζω εδώ μια δήλωση από την Gui_0
    private final Jgui_0 mainForm;

    //Variable για να κάνω το pagination. Το κάνω αρχική τιμή 20 γιατί το κουμπί search μου δίνει τα πρώτα 20
    private int nextPages =20;

    /**
     * Creates new form Jgui1_1
     *
     * @param mf
     */
    public Jgui1_1(Jgui_0 mf) {
        initComponents();
        //Εδώ κάνω την gui_0 parent της gui1_1 έτσι ώστε να μπορεί να γίνει setVisible(true);
        this.mainForm = mf;
        setTitle("WikiViewer - Search Article");
        setIconImage(pageIcon);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 600));

        //Μορφοποιώ τα κουμπιά
        jButton1.setText("Back");
        jButton1.setIcon(scaledIcon("/arrow-left.png", 13, 13));
        jButton2.setText("Search");
        jButton2.setIcon(scaledIcon("/search.png", 13, 13));
        jButton3.setText("Read full article");
        jButton3.setIcon(scaledIcon("/loading.png", 13, 13));
        jButton4.setText("Save to DB");
        jButton4.setIcon(scaledIcon("/download.png", 13, 13));
        jButton5.setText("Previous Results");
        jButton5.setIcon(scaledIcon("/angle-left.png", 13, 13));
        jButton6.setText("Next Results");
        jButton6.setIcon(scaledIcon("/angle-right.png", 13, 13));
        jButton6.setHorizontalTextPosition(SwingConstants.LEFT);

        jTextField1.setText(null);
        jTextField1.setPreferredSize(new Dimension(150, 26));

        //Φτιάχνω το instance για τα αποτελέσματα
        WikiApiClient api = new WikiApiClient();
        DefaultListModel listModel = new DefaultListModel();
        jList1.setModel(listModel);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Φτιάχνω τα events για τα mouse click
        jButton1.addActionListener(e -> {
            //Κάνω Dispose αυτήν που βρίσκομαι και εμφανίζω την προηγούμενη που είχα κρύψει
            this.dispose();
            mainForm.setVisible(true);
        });

        jButton2.addActionListener(e -> {
            final String keyword = jTextField1.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Type a keyword first.");
                return;
            }

            jButton2.setEnabled(false);
            jLabel1.setText("Searching Wikipedia...");

            new SwingWorker<List<WikiSearchResult>, Void>() {
                private Exception error;

                @Override
                protected List<WikiSearchResult> doInBackground() {
                    try {
                        return api.search(keyword, 20, 0);
                    } catch (Exception ex) {
                        error = ex;
                        return java.util.Collections.emptyList();
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (error != null) {
                            throw error;
                        }

                        List<WikiSearchResult> results = get();

                        // ✅ Load saved ids from DB so the renderer can show ✅
                        savedPageIds.clear();
                        try {
                            ArticleDAO dao = new ArticleDAO();
                            savedPageIds.addAll(dao.getAllSavedPageIds());
                        } catch (Exception dbEx) {
                            dbEx.printStackTrace(); // if DB check fails, list still shows normally
                        }

                        // Populate list
                        listModel.clear();
                        for (WikiSearchResult r : results) {
                            listModel.addElement(r);
                        }

                        // ✅ Refresh list display (forces renderer re-run)
                        jList1.repaint();

                        jLabel1.setText("Showing results " + 1 + " - " + 20);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        jLabel1.setText("Error.");
                        JOptionPane.showMessageDialog(Jgui1_1.this,
                                "API error: " + ex.getMessage());
                    } finally {
                        jButton2.setEnabled(true);
                    }
                }
            }.execute();
        });

        jButton6.addActionListener(e -> {
            final String keyword = jTextField1.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Type a keyword first.");
                return;
            }

            final int offsetToFetch = nextPages; // Κρατώ τον αριθμό της σελίδας
            nextPages += 20;                                 // Ετοιμάζω την επόμενη σελίδα

            jButton6.setEnabled(false);
            jLabel1.setText("Searching Wikipedia...");

            new SwingWorker<List<WikiSearchResult>, Void>() {
                private Exception error;

                @Override
                protected List<WikiSearchResult> doInBackground() {
                    try {
                        return api.search(keyword, 20, offsetToFetch);
                    } catch (Exception ex) {
                        error = ex;
                        return java.util.Collections.emptyList();
                    }
                }

                @Override
                protected void done() {
                    try {
                        if (error != null) {
                            throw error;
                        }

                        List<WikiSearchResult> results = get();

                        listModel.clear();
                        for (WikiSearchResult r : results) {
                            listModel.addElement(r);
                        }

                        jLabel1.setText("Showing results " + (offsetToFetch + 1) + " - " + (offsetToFetch + results.size()));

                    } catch (Exception ex) {
                        // αν αποτύχει, γύρνα πίσω το offset για να μη “χάσεις” σελίδα
                        nextPages = Math.max(0, nextPages - 20);

                        jLabel1.setText("Error.");
                        JOptionPane.showMessageDialog(Jgui1_1.this, "API error: " + ex.getMessage());
                    } finally {
                        jButton6.setEnabled(true);
                    }
                }
            }.execute();
        });
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setText("jButton2");

        jButton3.setText("jButton3");

        jButton4.setText("jButton4");

        jButton5.setText("jButton5");

        jButton6.setText("jButton6");

        jTextField1.setText("jTextField1");
        jTextField1.setMinimumSize(new java.awt.Dimension(75, 23));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jButton1.setText("jButton1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2))
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6)))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton2, jButton3, jButton4, jButton5, jButton6});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5)
                        .addComponent(jButton6)
                        .addComponent(jLabel2))
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
