/**
 *
 * @author Andriopoulos Xristos
 * @author Karagiannis Ioannis
 * @author Demisarlis Thomas
 */
package wikiviewer;

import java.util.List;
import view.MainForm;

/**
 * Κλάση κυρίως προγράμματος.
 */
public class WikiViewer {
    /**
     * Η αρχική φόρμα της εφαρμογής.
     */
    public static MainForm mainForm;

    /**
     * Οι κατηγορίες του πίνακα category.
     */
    public static List<String> categoryNames = List.of(
            "Αθλητισμός",
            "Επιστήμη",
            "Κοινωνία",
            "Οικονομία",
            "Παιδεία",
            "Πολιτική",
            "Πολιτισμός",
            "Τέχνη",
            "Τεχνολογία",
            "Υγεία",
            "Ιστορία");

    /**
     * Κυρίως πρόγραμμα.
     * 
     * @param args Οι παράμετροι από τη γραμμή εντολών.
     */
    public static void main(String[] args) {
        // Δημιουργία των πινάκων της βάσης δεδομένων αν δεν υπάρχουν
        DatabaseCreation.CreateDatabaseTables();

        // Δημιουργία του entity manager που χειρίζεται τη βάση δεδομένων
        DatabaseUse.createEntityManager();

        // Εισαγωγή των κατηγοριών στον πίνακα category αν δεν υπάρχουν
        DatabaseUse.insertCategories(categoryNames);

        // Δημιουργία και εμφάνιση της αρχικής φόρμας
        mainForm = new MainForm();
        mainForm.setVisible(true);
    }
}
