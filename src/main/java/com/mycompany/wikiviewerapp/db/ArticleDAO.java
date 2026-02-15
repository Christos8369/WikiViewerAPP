/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import Pojo.ArticleJohn;
import java.util.*;
import java.sql.*;
import com.mycompany.wikiviewerapp.model.Article;
import javax.persistence.TypedQuery;

/**
 *
 * @author andri
 */
public class ArticleDAO {

    //Χρήστο οι κλήσεις στον SQL πρέπει να γίνουν από αυτό το ριμάδι. Θα σου φτιάξω τις μεθόδους.
    public static EntityManager em = Persistence.createEntityManagerFactory("com.mycompany_WikiViewerAPP_jar_1.0-SNAPSHOTPU").createEntityManager();

    private static final String URL = "jdbc:derby:WikiViewerAPP_DB;create=true";

    public void insertArticle(Article a) throws SQLException {

        String sql = """
            INSERT INTO ARTICLE
            (PAGEID, TITLE, SIZE, SNIPPET, TEXT, WORD_COUNT)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, a.getPageId());
            ps.setString(2, a.getTitle());
            ps.setObject(3, a.getSize());         // nullable Integer
            ps.setString(4, a.getSnippet());
            ps.setString(5, a.getText());
            ps.setObject(6, a.getWordCount());    // use setObject if wordCount can be null

            ps.executeUpdate();
        }
    }

    public java.util.Set<Integer> getAllSavedPageIds() throws java.sql.SQLException {

        java.util.Set<Integer> ids = new java.util.HashSet<>();
        String sql = "SELECT PAGEID FROM ARTICLE";

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(URL); java.sql.PreparedStatement ps = conn.prepareStatement(sql); java.sql.ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt(1));
            }
        }

        return ids;
    }

    /*
        * checks if the article already exists in the DB ( by comparing the pageId )
        * returnd true if it exists and false if not
        * will be used in the insertArticle method
     */
    public boolean exists(int pageId) throws SQLException {
        String sql = "SELECT 1 FROM ARTICLE WHERE PAGEID = ?";

        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pageId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if at least one row found
            }
        }
    }

    //==== Μέθοδοι εισαγωγής σε πίνακα με JPA ====
    public void insertIntoTableWithJpa(ArticleJohn article) {
        try {
            article = new ArticleJohn();

            em.getTransaction().begin();

            em.persist(article);

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Can't store article in DB");
        }
    }
    
    //Εμφάνιση όλων των αποθηκευμένων αρχείων από την  βάση δεδομένων
    public List<ArticleJohn> allRecords() {
        TypedQuery<ArticleJohn> query
                = ArticleDAO.em.createNamedQuery("Article.findAll", ArticleJohn.class);

        return query.getResultList();
    }
}
