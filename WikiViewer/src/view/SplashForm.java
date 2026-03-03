package view;

/**
 * Splash Screen της εφαρμογής.
 * Εμφανίζεται για 4 δευτερόλεπτα κατά την εκκίνηση.
 */
public class SplashForm extends javax.swing.JFrame {

        public SplashForm() {
                initComponents();

                // Προσθήκη περιγράμματος για να φαίνεται ωραίο
                getRootPane().setBorder(
                                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100), 2));

                // Φόρτωση, Σμίκρυνση (Scaling) και Τοποθέτηση της εικόνας ως Background
                // (600x337 pixels)
                try {
                        java.net.URL imgURL = getClass().getResource("/resources/splash.jpg");
                        if (imgURL != null) {
                                javax.swing.ImageIcon originalIcon = new javax.swing.ImageIcon(imgURL);
                                java.awt.Image originalImage = originalIcon.getImage();

                                // Κάνουμε σμίκρυνση διατηρώντας το Aspect Ratio (π.χ. σε 600 πλάτος, 337 ύψος
                                // που είναι περίπου 16:9)
                                java.awt.Image scaledImage = originalImage.getScaledInstance(600, 337,
                                                java.awt.Image.SCALE_SMOOTH);
                                javax.swing.ImageIcon scaledIcon = new javax.swing.ImageIcon(scaledImage);

                                jLabel1.setIcon(scaledIcon);
                        }
                } catch (Exception e) {
                }

                // Προσθήκη εικονιδίου στην taskbar (αν βρεθεί)
                try {
                        java.awt.Image pageIcon = new javax.swing.ImageIcon(
                                        getClass().getResource("/resources/wikilogo.png")).getImage();
                        setIconImage(pageIcon);
                } catch (Exception e) {
                }
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jLabel1 = new javax.swing.JLabel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                setUndecorated(true);

                jLabel1.setForeground(new java.awt.Color(255, 255, 255));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("<html><center><h1 style=\"color: white;\">WikiViewer</h1><h3 style=\"color: white;\">Η εγκυκλοπαίδεια στον υπολογιστή σας</h3><br><br><i style=\"color: white;\">Φόρτωση δεδομένων...</i></center></html>");
                jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 600,
                                                                Short.MAX_VALUE));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 337,
                                                                Short.MAX_VALUE));

                pack();
                setLocationRelativeTo(null);
        }// </editor-fold>//GEN-END:initComponents

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JLabel jLabel1;
        // End of variables declaration//GEN-END:variables
}
