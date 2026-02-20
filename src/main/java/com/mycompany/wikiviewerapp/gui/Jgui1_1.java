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

    //
    private List<WikiSearchResult> currentResults = java.util.Collections.emptyList();

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

    private void fetchPage(String keyword, DefaultListModel listModel) {

        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        jButton2.setEnabled(false);
        jLabel1.setText("Searching Wikipedia...");

        new SwingWorker<List<WikiSearchResult>, Void>() {
            private Exception error;

            @Override
            protected List<WikiSearchResult> doInBackground() {
                try {
                    return api.search(keyword, PAGE_SIZE, offset);
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

                    savedPageIds.clear();
                    try {
                        ArticleDAO dao = new ArticleDAO();
                        savedPageIds.addAll(dao.getAllSavedPageIds());
                    } catch (Exception dbEx) {
                        dbEx.printStackTrace();
                    }

                    currentResults = get();

                    listModel.clear();
                    for (WikiSearchResult r : currentResults) {
                        listModel.addElement(r.toString()); // HTML string
                    }
                    jList1.repaint();

                    int from = results.isEmpty() ? 0 : offset + 1;
                    int to = offset + results.size();
                    jLabel1.setText("Showing results " + from + " - " + to);

                    // Buttons state
                    jButton5.setEnabled(offset > 0);

                    // Αν γύρισαν λιγότερα από PAGE_SIZE δεν υπάρχουν άλλες σελίδες
                    jButton6.setEnabled(results.size() == PAGE_SIZE);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    jLabel1.setText("Error.");
                    JOptionPane.showMessageDialog(Jgui1_1.this,
                            "API error: " + ex.getMessage());

                    // σε error, μην αφήνεις offset “χαλασμένο”
                    // (π.χ. αν πάτησαν Next και έσκασε)
                    offset = Math.max(0, offset);

                    jButton5.setEnabled(offset > 0);
                    jButton6.setEnabled(true);
                } finally {
                    jButton2.setEnabled(true);
                }
            }
        }.execute();
    }
    //Επειδή χρειάζομαι αυτή η φόρμα να ανοίγει και μετά να γίνεται dispose χωρίς να χάνω την Gui_0 βάζω εδώ μια δήλωση από την Gui_0
    private final Jgui_0 mainForm;

    private final WikiApiClient api = new WikiApiClient();

    private static final int PAGE_SIZE = 20;
    private int offset = 0;

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
        
        jLabel1.setText(null);
        jLabel2.setText(null);

        //Φτιάχνω το instance για τα αποτελέσματα
        // WikiApiClient api = new WikiApiClient();
        DefaultListModel listModel = new DefaultListModel();
        jList1.setModel(listModel);
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // = = = = Φτιάχνω τα events για τα mouse click = = = =
        //Είναι το κουμπί Back και επιστρέδω στην κεντρική οθόνη.
        jButton1.addActionListener(e -> {  //Είναι το κουμπί Back
            //Κάνω Dispose αυτήν που βρίσκομαι και εμφανίζω την προηγούμενη που είχα κρύψει
            this.dispose();
            mainForm.setVisible(true);
        });
        //Είναι το κουμπί Search
        jButton2.addActionListener(e -> {
            final String keyword = jTextField1.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Type a keyword first.");
                return;
            }

            offset = 0;                 // reset στην πρώτη σελίδα
            fetchPage(keyword, listModel);
        });
        //Είναι το κουμπί Read full Article
        jButton3.addActionListener(e -> {

            int index = jList1.getSelectedIndex();
            if (index < 0) {
                JOptionPane.showMessageDialog(this, "Διάλεξε ένα αποτέλεσμα πρώτα.");
                return;
            }

            int pageId = currentResults.get(index).getPageId();

            try {
                WikiApiClient fetch = new WikiApiClient();
                String article = fetch.fetchFullText(pageId);

                JTextPane textPane = new JTextPane();
                textPane.setText(article);
                textPane.setEditable(false);
                textPane.setCaretPosition(0);

                JScrollPane scrollPane = new JScrollPane(textPane);

                JDialog dialog = new JDialog(this, "Άρθρο Αριθμός: " + pageId + " Wikipedia", true);
                dialog.add(scrollPane);
                dialog.setSize(1200, 1000);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Σφάλμα φόρτωσης άρθρου.");
            }
        });

        //Είναι το κουμπί που φέρνει τα προηγούμενα αποτελέσματα
        jButton5.addActionListener(e -> {
            final String keyword = jTextField1.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Type a keyword first.");
                return;
            }

            offset = Math.max(0, offset - PAGE_SIZE);
            fetchPage(keyword, listModel);
        });
        //Είναι το κουμπί Next που φέρνει τα επόμενα αποτελέσματα
        jButton6.addActionListener(e -> {
            final String keyword = jTextField1.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Type a keyword first.");
                return;
            }
            offset += PAGE_SIZE;
            fetchPage(keyword, listModel);
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

        jButton5.setLabel("Previous");

        jButton6.setLabel("Next");

        jTextField1.setText("jTextField1");
        jTextField1.setMinimumSize(new java.awt.Dimension(75, 23));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jButton1.setLabel("Back");

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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
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
