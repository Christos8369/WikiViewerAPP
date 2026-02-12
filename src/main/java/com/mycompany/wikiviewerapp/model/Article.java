/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp.model;

/**
 *
 * @author andri
 */
public class Article {

    public Article(int pageId, String title1, Object par2) {
    }
    
    private int pageId;
    private String title;
    private int size;
    private String category;
    private int rating;     // from 0 to 5 only 
    private String snippet;
    private String text;    // limit of 2 GB 
    private int wordCount;

    public int getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public String getCategory() {
        return category;
    }

    public int getRating() {
        return rating;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getText() {
        return text;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setPageid(int pageid) {
        this.pageId = pageid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public Article(int pageId, String title) {
        this.pageId = pageId;
        this.title = title;
    }
    
    

    
    
    

    
}
