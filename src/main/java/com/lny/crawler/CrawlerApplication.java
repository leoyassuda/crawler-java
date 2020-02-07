package com.lny.crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Scanner;

@SpringBootApplication
public class CrawlerApplication {

    private final static String filename = "top-votes-reddit.txt";

    public static void main(String[] args) {
        SpringApplication.run(CrawlerApplication.class, args);

        String subRedditParams;

        Scanner s = new Scanner(System.in);
        System.out.println("Insira os subreddits para fazer a varredura dos dados e separados por ponto e virgula\n" +
                "Por exemplo: 'nome1;nome2;nome3'");
        subRedditParams = s.next();
        System.out.println(subRedditParams);

        //Inicializando bot
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new TelegramBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        if (subRedditParams.length() > 0) {
            Extractor bwc = new Extractor();
            bwc.getPageLinks(subRedditParams);
            bwc.getArticles();
            bwc.writeToFile(filename);
        } else {
            System.out.println("Invalid arguments");
        }


    }

}
