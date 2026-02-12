/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.wikiviewerapp.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author andri
 */
public class WikiApiClient {

    private final OkHttpClient client = new OkHttpClient();

    public List<WikiSearchResult> search(String keyword, int limit, int offset) throws Exception {
        String encoded = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = "https://el.wikipedia.org/w/api.php?action=query&list=search"
                + "&srsearch=" + encoded
                + "&srlimit=" + limit
                + "&sroffset=" + offset
                + "&format=json";

    // ... same OkHttp + JSON parsing as you already have ...



        Request request = new Request.Builder()
            .url(url)
            .header("User-Agent", "WikiViewer/1.0 (student project)")
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new RuntimeException("HTTP error: " + response.code());
            }

            String json = response.body().string();

            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject query = root.getAsJsonObject("query");
            JsonArray searchArr = query.getAsJsonArray("search");

            List<WikiSearchResult> out = new ArrayList<>();
            
            for (int i = 0; i < searchArr.size(); i++) {
                JsonObject item = searchArr.get(i).getAsJsonObject();

                int pageId = item.get("pageid").getAsInt();
                String title = item.get("title").getAsString();
                String snippet = item.get("snippet").getAsString();

                int size = item.has("size") ? item.get("size").getAsInt() : 0;
                int wordCount = item.has("wordcount") ? item.get("wordcount").getAsInt() : 0;

                out.add(new WikiSearchResult(pageId, title, snippet, size, wordCount));
            }

            
            return out;
        }
    }
    
    public String fetchFullText(int pageId) throws Exception {
    String url = "https://el.wikipedia.org/w/api.php?action=query&prop=extracts"
            + "&pageids=" + pageId
            + "&explaintext=1"
            + "&format=json";

    Request request = new Request.Builder()
            .url(url)
            .header("User-Agent", "WikiViewer/1.0 (student project)")
            .build();

    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful() || response.body() == null) {
            throw new RuntimeException("HTTP error: " + response.code());
        }

        String json = response.body().string();

        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonObject pages = root.getAsJsonObject("query").getAsJsonObject("pages");
        JsonObject pageObj = pages.entrySet().iterator().next().getValue().getAsJsonObject();

        // "extract" contains the article text
        return pageObj.has("extract") ? pageObj.get("extract").getAsString() : "";
    }
}
    //ArticleDao articleDao = new ArticleDao();

    


    // Optional: remove HTML tags from snippet quickly
    public static String stripHtml(String s) {
        return s == null ? null : s.replaceAll("<[^>]*>", "");
    }
}
