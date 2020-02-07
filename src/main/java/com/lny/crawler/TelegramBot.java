package com.lny.crawler;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText(update.getMessage().getText());

            String incomingMessage = update.getMessage().getText();

            if (incomingMessage.length() > 0) {

                String[] caller = incomingMessage.split(" ");

                switch (caller[0]) {
                    case "/start":
                        message = this.start(update);
                        break;
                    case "/search":
                        message = this.crawlerExecutor(update, caller[1].isEmpty() ? "" : caller[1]);
                        break;
                    default:
                        System.out.println("Command not found.");
                }
            }

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "YassudaCrawlerBot";
    }

    @Override
    public String getBotToken() {
        return "612692608:AAHXWLmgB4xCtWxlUk7veZPSUVeQpxdAxtk";
    }

    private SendMessage start(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText("Olá " + update.getMessage().getFrom().getFirstName() + "\n" +
                        "Este é um bot para executar um tarefa de varrer o reddit e listar tópicos em alta." + "\n" +
                        "único comando existente no momento: \n" +
                        "/search [lista dos subReddits sem colchetes e separados por ponto e virgula] - Exemplo: /search catz;brazil;AskReddit");
    }

    private SendMessage crawlerExecutor(Update update, String subRedditParams) {
        SendMessage message = new SendMessage();
        if (subRedditParams.length() > 0) {
            Extractor bwc = new Extractor();
            bwc.getPageLinks(subRedditParams);
            bwc.getArticles();
            message = bwc.sendMessage();
        } else {
            message.setText("Faltou parâmetros:\n" +
                    "/search [lista dos subReddits sem colchetes e separados por ponto e virgula] - Exemplo: /search catz;brazil;AskReddit");
        }

        message.setChatId(update.getMessage().getChatId());
        return message;


    }


}
