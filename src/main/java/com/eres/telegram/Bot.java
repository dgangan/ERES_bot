package com.eres.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class Bot extends TelegramLongPollingBot {


    MessageHandler msgHandler;
    public Bot() throws SQLException, IOException {
        this.msgHandler = new MessageHandler();
    }
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId().toString());
            String replyText = msgHandler.startCommand(update);
            message.setText(replyText);
            message.enableHtml(true);

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    //@Override
    public String getBotUsername() {
        return "eres_test_bot";
    }

    @Override
    public String getBotToken() {
        return "xxx";
    }

}
