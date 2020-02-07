package com.lny.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Extractor {

    private HashSet<String> links;
    private List<List<String>> articles;

    private final String URL = "https://www.reddit.com/r/";
    private final String PARAMS = "/top/?t=day";

    public Extractor() {
        links = new HashSet<>();
        articles = new ArrayList<>();
    }

    public void getPageLinks(String subReddit) {
        for (String s : subReddit.split(";")) {
            links.add(URL + s + PARAMS);
        }
    }

    public void getArticles() {
        Integer count = 0;
        links.forEach(linkSubReddit -> {
            Document document;
            try {
                document = Jsoup.connect(linkSubReddit).timeout(15000).get();

                Elements articleLinks = document.select("div.scrollerItem.Post");
                for (Element article : articleLinks) {

                    Elements voteCount = article.select("div.scrollerItem.Post div div button + div");
                    Elements titleArticle = article.select("div.scrollerItem.Post div + div div + div h3");
                    Elements linkArticle = article.select("div.scrollerItem.Post div + div div + div a");
                    Elements linkComments = article.select("div.scrollerItem.Post div + div div + div + div + div div a");

                    if (voteCount.text().matches("(\\d*.\\d*)([kK])")) {
                        Double dUpFiveK = Double.valueOf(voteCount.text().substring(0, voteCount.text().toLowerCase().indexOf("k")));
                        if (dUpFiveK > 5.0) {
                            ArrayList<String> temporary = new ArrayList<>();
                            temporary.add(voteCount.text()); //upvotes
                            temporary.add(linkSubReddit.substring(linkSubReddit.lastIndexOf(".com/r/") + 7, linkSubReddit.indexOf(PARAMS))); //subreddit
                            temporary.add(titleArticle.text()); //titulo
                            temporary.add(linkComments.attr("abs:href")); //link comentarios
                            temporary.add(linkArticle.attr("abs:href")); //link subreddit
                            articles.add(temporary);
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    public void writeToFile(String filename) {
        FileWriter writer;
        try {
            writer = new FileWriter(filename);
            System.out.println("Salvando em arquivo: " + filename);
            articles.forEach(art -> {
                try {
                    String temp = new StringBuilder().append("- Número de Upvotes: ").append(art.get(0))
                            .append(" | SubReddit: ").append(art.get(1))
                            .append(" | Título: ").append(art.get(2))
                            .append(" | link comentários: ").append(art.get(3))
                            .append(" | link SubReddit: ").append(art.get(4))
                            .append("\n").toString();
                    System.out.println(temp);
                    writer.write(temp);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public SendMessage sendMessage() {
        StringBuilder sb = new StringBuilder();

        if (articles.isEmpty()) {
            articles.forEach(art -> sb.append("- Número de Upvotes: ").append(art.get(0))
                    .append(" | SubReddit: ").append(art.get(1))
                    .append(" | Título: ").append(art.get(2))
                    .append(" | link comentários: ").append(art.get(3))
                    .append(" | link SubReddit: ").append(art.get(4)).append("\n"));
        } else {
            sb.append("Não foram encontrados resultados...");
        }
        return new SendMessage().setText(sb.toString());
    }
}
