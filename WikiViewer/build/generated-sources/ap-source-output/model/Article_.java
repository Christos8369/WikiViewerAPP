package model;

import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Category;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-02-22T18:59:07", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Article.class)
public class Article_ { 

    public static volatile SingularAttribute<Article, String> snippet;
    public static volatile SingularAttribute<Article, String> comments;
    public static volatile SingularAttribute<Article, Integer> articleid;
    public static volatile SingularAttribute<Article, Integer> rating;
    public static volatile SingularAttribute<Article, Date> savedat;
    public static volatile SingularAttribute<Article, Integer> pageid;
    public static volatile SingularAttribute<Article, String> title;
    public static volatile SingularAttribute<Article, String> content;
    public static volatile SingularAttribute<Article, Category> categoryid;
    public static volatile SingularAttribute<Article, Date> timestamp;

}