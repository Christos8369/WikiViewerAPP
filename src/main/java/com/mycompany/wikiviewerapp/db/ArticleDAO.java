package com.mycompany.wikiviewerapp.db;

import Pojo.Article;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArticleDAO {

    private static final EntityManagerFactory emf
            = Persistence.createEntityManagerFactory("com.mycompany_WikiViewerAPP_jar_1.0-SNAPSHOTPU");

    private EntityManager em() {
        return emf.createEntityManager();
    }

    // ===== Load all saved PAGEIDs =====
    public Set<Integer> getAllSavedPageIds() {
        EntityManager em = em();
        try {
            List<Integer> ids = em.createQuery(
                    "SELECT a.pageid FROM Article a", Integer.class
            ).getResultList();
            return new HashSet<>(ids);
        } finally {
            em.close();
        }
    }

    // ===== Save (insert or update) =====
    public void saveOrUpdate(Article article) {
        EntityManager em = em();
        try {
            em.getTransaction().begin();
            em.merge(article);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // ===== Exists by pageId =====
    public boolean existsByPageId(int pageId) {
        EntityManager em = em();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(a) FROM Article a WHERE a.pageid = :pid", Long.class
            ).setParameter("pid", pageId)
                    .getSingleResult();

            return count != null && count > 0;
        } finally {
            em.close();
        }
    }

    // ===== Insert only (fails if already exists depending on PK) =====
    public void insert(Article article) {
        EntityManager em = em();
        try {
            em.getTransaction().begin();
            em.persist(article);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    // ===== Fetch all records =====
    public List<Article> allRecords() {
        EntityManager em = em();
        try {
            // Αν ΔΕΝ έχεις NamedQuery, χρησιμοποίησε απλό JPQL:
            TypedQuery<Article> q = em.createNamedQuery("Article.findAll", Article.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}

// <editor-fold desc="Όχι ενδεδειγμένος τρόπος για επικοινωνία με την Βάση Δεδομένων. Αυτός είναι JDBC">
//    private static final String URL = "jdbc:derby:WikiViewerAPP_DB;create=true";
//    public void insertArticle(Article a) throws SQLException {
//
//        String sql = """
//            INSERT INTO ARTICLE
//            (PAGEID, TITLE, SIZE, SNIPPET, TEXT, WORD_COUNT)
//            VALUES (?, ?, ?, ?, ?, ?)
//            """;
//
//        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, a.getPageid());
//            ps.setString(2, a.getTitle());
//            ps.setObject(3, a.getSize());         // nullable Integer
//            ps.setString(4, a.getSnippet());
//            ps.setString(5, a.getText());
//            ps.setObject(6, a.getWordCount());    // use setObject if wordCount can be null
//
//            ps.executeUpdate();
//        }
//    }
//
//    public java.util.Set<Integer> getAllSavedPageIds() throws java.sql.SQLException {
//
//        java.util.Set<Integer> ids = new java.util.HashSet<>();
//        String sql = "SELECT PAGEID FROM ARTICLE";
//
//        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL); java.sql.PreparedStatement ps = conn.prepareStatement(sql); java.sql.ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                ids.add(rs.getInt(1));
//            }
//        }
//
//        return ids;
//    }
//
//    /*
//        * checks if the article already exists in the DB ( by comparing the pageId )
//        * returnd true if it exists and false if not
//        * will be used in the insertArticle method
//     */
//    public boolean exists(int pageId) throws SQLException {
//        String sql = "SELECT 1 FROM ARTICLE WHERE PAGEID = ?";
//
//        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setInt(1, pageId);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                return rs.next(); // true if at least one row found
//            }
//        }
//    }
// </editor-fold>

