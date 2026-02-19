/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pojo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ioann
 */
@Entity
@Table(name = "ARTICLE")
@NamedQueries({
    @NamedQuery(name = "Article.findAll", query = "SELECT a FROM Article a"),
    @NamedQuery(name = "Article.findByPageid", query = "SELECT a FROM Article a WHERE a.pageid = :pageid"),
    @NamedQuery(name = "Article.findByTitle", query = "SELECT a FROM Article a WHERE a.title = :title"),
    @NamedQuery(name = "Article.findBySize", query = "SELECT a FROM Article a WHERE a.size = :size"),
    @NamedQuery(name = "Article.findByCategory", query = "SELECT a FROM Article a WHERE a.category = :category"),
    @NamedQuery(name = "Article.findByRating", query = "SELECT a FROM Article a WHERE a.rating = :rating"),
    @NamedQuery(name = "Article.findBySnippet", query = "SELECT a FROM Article a WHERE a.snippet = :snippet"),
    @NamedQuery(name = "Article.findByWordCount", query = "SELECT a FROM Article a WHERE a.wordCount = :wordCount")})
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PAGEID")
    private Integer pageid;
    @Basic(optional = false)
    @Column(name = "TITLE")
    private String title;
    @Column(name = "SIZE")
    private Integer size;
    @Basic(optional = false)
    @Column(name = "CATEGORY")
    private String category;
    @Column(name = "RATING")
    private Integer rating;
    @Column(name = "SNIPPET")
    private String snippet;
    @Lob
    @Column(name = "TEXT")
    private String text;
    @Column(name = "WORD_COUNT")
    private Integer wordCount;

    public Article() {
    }

    public Article(Integer pageid) {
        this.pageid = pageid;
    }

    public Article(Integer pageid, String title, String category) {
        this.pageid = pageid;
        this.title = title;
        this.category = category;
    }

    public Integer getPageid() {
        return pageid;
    }

    public void setPageid(Integer pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pageid != null ? pageid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.pageid == null && other.pageid != null) || (this.pageid != null && !this.pageid.equals(other.pageid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pojo.Article[ pageid=" + pageid + " ]";
    }
    
}
