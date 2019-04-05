package kpi.acts.appz.bot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public abstract class Bot extends TelegramLongPollingBot {
    private final static Map<String, Bot> INSTANCES = new HashMap<>();
    private final static TelegramBotsApi botApi = new TelegramBotsApi();

    private final String token, botName;
    static {
        ApiContextInitializer.init();
    }

    protected Bot(String token, String botName){
        this.token = token;
        this.botName = botName;
    }

    protected static void runBot(Bot newBot) {
        if(newBot != null) {
            try {
                botApi.registerBot(newBot);
                INSTANCES.put(newBot.getBotToken(), newBot);
            } catch (TelegramApiException e) {
                newBot.processTheException(e);
            }
        }
    }

    public Message sendTextMessage(Message messageFrom, String text){
        try {
            SendMessage send = new SendMessage().setChatId(messageFrom.getChatId());

            send.setText(text.trim());
            return execute(send);
        } catch (Exception e) {
            processTheException(e);
            return null;
        }
    }

    protected abstract void processTheException(Exception e);

    public static Bot getInstance(String token){
        return INSTANCES.get(token);
    }

    @Override
    public final String getBotUsername() {
        return botName;
    }

    @Override
    public final String getBotToken() {
        return token;
    }
}
