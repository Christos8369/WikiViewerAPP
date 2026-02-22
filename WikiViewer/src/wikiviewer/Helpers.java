package wikiviewer;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

//Κλάση βοηθητικών μεθόδων
public class Helpers {  
    
    //Μέθοδος που διαμορφώνει ημερομηνία και ώρα στο επιθυμητό format
    public static String getFormattedTimestamp(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    
    //Μέθοδος που κανονικοποιεί μια συμβολοσειρά 
    //Αφαιρεί τόνους και άλλα διακριτικά σημεία και μετατρέπει όλους τους χαρακτήρες σε πεζούς 
    public static String normalizeForSearch(String string) {
        if (string == null) return "";
        return Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase();
    }
    
    //Μέθοδος που λαμβάνει τους χαρακτήρες μιας συμβολοσειράς μέχρι ένα μέγιστο πλήθος χαρακτήρων 
    public static String getString(String string, int maxCharacters) {
        return string.substring(0, Math.min(maxCharacters, string.length()));
    }
}
