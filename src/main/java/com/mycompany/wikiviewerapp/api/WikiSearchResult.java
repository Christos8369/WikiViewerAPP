/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp.api;

/**
 *
 * @author andri
 */
public class WikiSearchResult {

    private final int pageId;
    private final String title;
    private final String snippet;
    private final int size;
    private final int wordCount;

    public WikiSearchResult(int pageId, String title, String snippet,
                            int size, int wordCount) {
        this.pageId = pageId;
        this.title = title;
        this.snippet = snippet;
        this.size = size;
        this.wordCount = wordCount;
    }

    public int getPageId() { return pageId; }
    public String getTitle() { return title; }
    public String getSnippet() { return snippet; }
    public int getSize() { return size; }
    public int getWordCount() { return wordCount; }

    @Override
    public String toString() {
        return title;
    }
}
