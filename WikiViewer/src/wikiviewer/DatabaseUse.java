/**
 *
 * @author Andriopoulos Xristos
 * @author Karagiannis Ioannis
 * @author Demisarlis Thomas
 */
package wikiviewer;

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import model.Article;
import model.Category;
import model.Search;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import java.util.Date;;

//Κλάση για την διαχείριση της βάσης δεδομένων
public class DatabaseUse {

    //O entity manager για την διαχείριση της βάσης δεδομένων
    public static EntityManager em;

    //Δημιουργεί τον entity manager
    public static void createEntityManager() {
        em = Persistence.createEntityManagerFactory("WikiViewerPU").createEntityManager();
    }

    //Query που επιστρέφει όλες τις κατηγορίες άρθρων ταξινομημένες ως προς το όνομα
    public static List<Category> getCategories() {
        Query q = em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class);
        List<Category> categories = q.getResultList();
        return categories;
    }
    
    //Query που επιστρέφει όλα τα άρθρα ταξινομημένα με φθίνουσα διάταξη ως προς την ημερομηνία αποθήκευσης
    public static List<Article> getArticles() {
        Query q = em.createQuery("SELECT a FROM Article a ORDER BY a.savedat DESC", Article.class);
        List<Article> articles = q.getResultList();
        return articles;
    }
    
    //Query που επιστρέφει όλες τις αναζητήσεις ταξινομημένες με φθίνουσα διάταξη ως προς το πλήθος αναζητήσεων
    //και αύξουσα διάταξη ως προς το κείμενο αναζήτησης για όσες έχουν το ίδιο πλήθος αναζητήσεων    
    public static List<Search> getSearches() {
        Query q = em.createQuery("SELECT s from Search s order by s.numberofsearches desc, s.searchstring", Search.class);
        return q.getResultList();
    }
    
    //Query που αναζητά και επιστρέφει την κατηγορία με βάση κάποιο όνομα (ή null αν δεν την βρει)
    public static Category getCategory(String name) {
        Query q = em.createNamedQuery("Category.findByName", Category.class);
        q.setParameter("name", name); 
        try {
            Category category = (Category) q.getSingleResult();
            return category;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    //Query που αναζητά και επιστρέφει το άρθρο με βάση κάποιον αριθμό σελίδας (ή null αν δεν το βρει)
    public static Article getArticle(int pageId) {
        Query q = em.createNamedQuery("Article.findByPageid", Article.class);
        q.setParameter("pageid", pageId); 
        try {
            Article article = (Article) q.getSingleResult();
            return article;
        } catch (NoResultException e) {
            return null;
        }
    }
        
    //Query που αναζητά και επιστρέφει την αναζήτηση με βάση κάποιο κείμενο αναζήτησης (ή null αν δεν την βρει)
    public static Search getSearch(String searchString) {
        Query q = em.createNamedQuery("Search.findBySearchstring", Search.class);
        q.setParameter("searchstring", searchString); 
        try {
            Search search = (Search) q.getSingleResult();
            return search;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    //Query που επιστρέφει το πλήθος αποθηκευμένων άρθρων για κάθε κατηγορία (στατιστικά κατηγορίας)
    //Τα στατιστικά επιστρέφονται με φθίνουσα διάταξη ως προς το πλήθος αποθηκευμένων άρθρων
    //και αύξουσα διάταξη ως προς το όνομα κατηγορίας για όσες έχουν το ίδιο πλήθος αποθηκευμένων άρθρων 
    public static List<Object[]> getCategoryStatistics() {
        Query q = em.createQuery("SELECT c.name, COUNT(a) FROM Category c LEFT JOIN c.articleList a GROUP BY c.name ORDER BY COUNT(a) DESC, c.name ASC");
        List<Object[]> statistics = q.getResultList();
        return statistics;
    }
    
    //Query που επιστρέφει τα αποθηκευμένα άρθρα για μία κατηγορία
    public static List<Article> getArticlesForCategory(Category category) {
        Query q = em.createQuery("SELECT a FROM Article a WHERE a.categoryid = :category ORDER BY a.savedat DESC", Article.class);
        q.setParameter("category", category);
        List<Article> articles = q.getResultList();
        return articles;
    }
    
    //Αποθηκεύει ένα άρθρο με timestamp την τρέχουσα ημερομηνία/ώρα και το επιστρέφει
    public static Article storeArticle(Article article) {
        em.getTransaction().begin();
        article.setSavedat(new Date());
        em.persist(article);
        em.getTransaction().commit();
        return article;
    }
    
    //Αποθηκεύει μία αναζήτηση
    //Αν η αναζήτηση υπάρχει της αυξάνει κατά 1 το πλήθος αναζητήσεων
    //ενώ αν δεν υπάρχει την δημιουργεί με πλήθος αναζητήσεων = 1
    public static void storeSearch(String searchString) {
        Search search = getSearch(searchString);
        if (search != null) {
            search.setNumberofsearches(search.getNumberofsearches() + 1);
        } else {
            search = new Search(null, searchString, 1); 
        }
        em.getTransaction().begin();
        em.persist(search);
        em.getTransaction().commit();
    }
    
    //Εισάγει κατηγορίες στο πίνακα categories αν δεν υπάρχουν ήδη
    public static void insertCategories(List<String> categoryNames) {
        if (!getCategories().isEmpty()) {
            return;
        }
        em.getTransaction().begin();
        for(String categoryName : categoryNames) {
            Category category = new Category(null, categoryName);
            em.persist(category);
        }
        em.getTransaction().commit();
    }
    
        // Διαγράφει ένα άρθρο από τη βάση με βάση το pageid
    // Επιστρέφει true αν διαγράφηκε, false αν δεν βρέθηκε
    public static boolean deleteArticleByPageid(int pageid) {
        Article article = getArticle(pageid);  // χρησιμοποιεί το NamedQuery Article.findByPageid

        if (article == null) {
            return false;
        }

        em.getTransaction().begin();
        em.remove(article);          // article είναι managed γιατί ήρθε από query
        em.getTransaction().commit();

        return true;
}
}
