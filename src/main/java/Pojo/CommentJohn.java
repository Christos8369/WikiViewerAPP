/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pojo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author ioann
 */
@Entity
@Table(name = "COMMENT")
@NamedQueries({
    @NamedQuery(name = "Comment.findAll", query = "SELECT c FROM Comment c"),
    @NamedQuery(name = "Comment.findByCommentId", query = "SELECT c FROM Comment c WHERE c.commentId = :commentId"),
    @NamedQuery(name = "Comment.findByTextOfComment", query = "SELECT c FROM Comment c WHERE c.textOfComment = :textOfComment")})
public class CommentJohn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COMMENT_ID")
    private Integer commentId;
    @Basic(optional = false)
    @Column(name = "TEXT_OF_COMMENT")
    private String textOfComment;
    @JoinColumn(name = "ARTICLE_PAGE_ID", referencedColumnName = "PAGEID")
    @ManyToOne(optional = false)
    private ArticleJohn articlePageId;

    public CommentJohn() {
    }

    public CommentJohn(Integer commentId) {
        this.commentId = commentId;
    }

    public CommentJohn(Integer commentId, String textOfComment) {
        this.commentId = commentId;
        this.textOfComment = textOfComment;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getTextOfComment() {
        return textOfComment;
    }

    public void setTextOfComment(String textOfComment) {
        this.textOfComment = textOfComment;
    }

    public ArticleJohn getArticlePageId() {
        return articlePageId;
    }

    public void setArticlePageId(ArticleJohn articlePageId) {
        this.articlePageId = articlePageId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (commentId != null ? commentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CommentJohn)) {
            return false;
        }
        CommentJohn other = (CommentJohn) object;
        if ((this.commentId == null && other.commentId != null) || (this.commentId != null && !this.commentId.equals(other.commentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pojo.Comment[ commentId=" + commentId + " ]";
    }
    
}
