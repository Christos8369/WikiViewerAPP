package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Article;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2026-02-22T18:59:07", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile ListAttribute<Category, Article> articleList;
    public static volatile SingularAttribute<Category, String> name;
    public static volatile SingularAttribute<Category, Integer> categoryid;

}