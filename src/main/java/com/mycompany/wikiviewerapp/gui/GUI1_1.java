//package com.mycompany.wikiviewerapp.gui;
//import com.mycompany.wikiviewerapp.api.WikiApiClient;
//import com.mycompany.wikiviewerapp.api.WikiSearchResult;
//import com.mycompany.wikiviewerapp.db.ArticleDAO;
//import Pojo.Article;
//import javax.swing.*;
//import java.awt.*;
//import java.util.List;
//
//public class GUI1_1 extends JFrame {
//
//    private final java.util.Set<Integer> savedPageIds = new java.util.HashSet<>();
//
//    private JList<WikiSearchResult> resultsList;
//
//    // helper inside GUI class
//    private static String shortSnippet(String s) {
//        s = WikiApiClient.stripHtml(s);
//        if (s == null) return "";
//        s = s.replaceAll("\\s+", " ").trim(); //Regex Expression
//        return (s.length() > 180) ? s.substring(0, 180) + "…" : s;
//    }
//
//    public GUI1_1() {
//        setTitle("WikiViewer - Search Article");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLayout(new BorderLayout(10, 10));
//
//        // ===== Top panel =====
//        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JButton backButton = new JButton("← Back");
//        JLabel searchLabel = new JLabel("Search article:");
//        JTextField searchField = new JTextField(25);
//        JButton searchButton = new JButton("Search");
//        JButton readButton = new JButton("Read full article");
//        JButton saveButton = new JButton("Save to DB");
//
//        searchPanel.add(backButton);
//        searchPanel.add(searchLabel);
//        searchPanel.add(searchField);
//        searchPanel.add(searchButton);
//        searchPanel.add(readButton);
//        searchPanel.add(saveButton);
//
//        // ===== Center panel (results list) =====
//        JPanel resultsPanel = new JPanel(new BorderLayout());
//        resultsPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
//
//        DefaultListModel<WikiSearchResult> listModel = new DefaultListModel<>();
//
//        // ✅ assign to FIELD (no local variable)
//        resultsList = new JList<>(listModel);
//        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        // ✅ renderer goes RIGHT HERE
//        resultsList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
//
//            boolean isSaved = savedPageIds.contains(value.getPageId());
//
//            String snippet = shortSnippet(value.getSnippet());
//
//            String titleHtml = (isSaved ? "✅ " : "") + value.getTitle();
//
//            String html = "<html>"
//                    + "<b>" + titleHtml + "</b>"
//                    + " &nbsp; <span style='color:gray'>"
//                    + "(pageId: " + value.getPageId()
//                    + ", words: " + value.getWordCount()
//                    + ", size: " + value.getSize()
//                    + ")</span><br>"
//                    + snippet
//                    + "</html>";
//
//            JLabel label = new JLabel(html);
//            label.setOpaque(true);
//            label.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
//
//            if (isSelected) {
//                label.setBackground(list.getSelectionBackground());
//                label.setForeground(list.getSelectionForeground());
//            } else {
//                label.setBackground(list.getBackground());
//                label.setForeground(list.getForeground());
//            }
//            return label;
//        });
//        
//
//        JScrollPane scrollPane = new JScrollPane(resultsList);
//        resultsPanel.add(scrollPane, BorderLayout.CENTER);
//
//        // ===== Bottom panel =====
//        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JLabel statusLabel = new JLabel("Ready.");
//        statusPanel.add(statusLabel);
//
//        add(searchPanel, BorderLayout.NORTH);
//        add(resultsPanel, BorderLayout.CENTER);
//        add(statusPanel, BorderLayout.SOUTH);
//
//        // ===== API client (reuse) =====
//        WikiApiClient api = new WikiApiClient();
//
//        // ===== Search button =====
//        searchButton.addActionListener(e -> {
//          final String keyword = searchField.getText().trim();
//          if (keyword.isEmpty()) {
//              JOptionPane.showMessageDialog(this, "Type a keyword first.");
//              return;
//          }
//
//          searchButton.setEnabled(false);
//          statusLabel.setText("Searching Wikipedia...");
//
//          new SwingWorker<List<WikiSearchResult>, Void>() {
//              private Exception error;
//
//              @Override
//              protected List<WikiSearchResult> doInBackground() {
//                  try {
//                      return api.search(keyword, 50, 0);
//                  } catch (Exception ex) {
//                      error = ex;
//                      return java.util.Collections.emptyList();
//                  }
//              }
//
//              @Override
//              protected void done() {
//                  try {
//                      if (error != null) throw error;
//
//                      List<WikiSearchResult> results = get();
//
//                      // ✅ Load saved ids from DB so the renderer can show ✅
//                      savedPageIds.clear();
//                      try {
//                          ArticleDAO dao = new ArticleDAO();
//                          savedPageIds.addAll(dao.getAllSavedPageIds());
//                      } catch (Exception dbEx) {
//                          dbEx.printStackTrace(); // if DB check fails, list still shows normally
//                      }
//
//                      // Populate list
//                      listModel.clear();
//                      for (WikiSearchResult r : results) {
//                          listModel.addElement(r);
//                      }
//
//                      // ✅ Refresh list display (forces renderer re-run)
//                      resultsList.repaint();
//
//                      statusLabel.setText("Found " + results.size() + " results.");
//
//                  } catch (Exception ex) {
//                      ex.printStackTrace();
//                      statusLabel.setText("Error.");
//                      JOptionPane.showMessageDialog(GUI1_1.this,
//                              "API error: " + ex.getMessage());
//                  } finally {
//                      searchButton.setEnabled(true);
//                  }
//              }
//          }.execute();
//      });
//
//
//        // ===== Read full article =====
//        readButton.addActionListener(e -> {
//            WikiSearchResult selected = resultsList.getSelectedValue();
//            if (selected == null) {
//                JOptionPane.showMessageDialog(this, "Select an article first.");
//                return;
//            }
//
//            int pageId = selected.getPageId();
//            String title = selected.getTitle();
//
//            readButton.setEnabled(false);
//            statusLabel.setText("Loading full article...");
//
//            new SwingWorker<String, Void>() {
//                @Override
//                protected String doInBackground() throws Exception {
//                    return api.fetchFullText(pageId);
//                }
//
//                @Override
//                protected void done() {
//                    try {
//                        String text = get();
//
//                        JFrame reader = new JFrame("WikiViewer - " + title);
//                        reader.setSize(800, 600);
//                        reader.setLocationRelativeTo(GUI1_1.this);
//
//                        JTextArea area = new JTextArea(text);
//                        area.setEditable(false);
//                        area.setLineWrap(true);
//                        area.setWrapStyleWord(true);
//
//                        reader.add(new JScrollPane(area));
//                        reader.setVisible(true);
//
//                        statusLabel.setText("Loaded.");
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                        statusLabel.setText("Error.");
//                        JOptionPane.showMessageDialog(GUI1_1.this, "Error: " + ex.getMessage());
//                    } finally {
//                        readButton.setEnabled(true);
//                    }
//                }
//            }.execute();
//        });
//
//        // ===== Save to DB (with text) =====
//        saveButton.addActionListener(e -> {
//
//            WikiSearchResult selected = resultsList.getSelectedValue();
//            if (selected == null) {
//                JOptionPane.showMessageDialog(this, "Select an article first.");
//                return;
//            }
//
//            try {
//                // Fetch full article text from API
//                String fullText = api.fetchFullText(selected.getPageId());
//
//                // Create Article model object
//                Article a = new Article();
//                a.setSnippet(WikiApiClient.stripHtml(selected.getSnippet()));
//                a.setSize(selected.getSize());
//                a.setWordCount(selected.getWordCount());
//                a.setText(fullText);
//
//                // Insert into DB
//                ArticleDAO dao = new ArticleDAO();
//                dao.insertArticle(a);
//
//                // 🔹 Mark as saved locally
//                savedPageIds.add(selected.getPageId());
//
//                // 🔹 Refresh renderer so ✅ appears immediately
//                resultsList.repaint();
//
//                JOptionPane.showMessageDialog(this, "Saved to DB (with text).");
//
//            } catch (Exception ex) {
//
//                if (ex instanceof java.sql.SQLException
//                        && "23505".equals(((java.sql.SQLException) ex).getSQLState())) {
//
//                    JOptionPane.showMessageDialog(this, "This article is already saved.");
//
//                } else {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
//                }
//            }
//        });
//
//
//        // ===== Back =====
//        backButton.addActionListener(e -> {
//            new gui_0();
//            dispose();
//        });
//
//        // ✅ show window once
//        pack();
//        setLocationRelativeTo(null);
//        setMinimumSize(getPreferredSize());
//        setVisible(true);
//    }
//}
