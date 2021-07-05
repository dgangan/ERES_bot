package com.eres.telegram.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class AbstractCommand extends BotCommand {

    AbstractCommand(String identifier, String description) {
        super(identifier, description);
    }

    void sendTextAnswer(AbsSender absSender, Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.enableHtml(true);
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    void sendAnswer(AbsSender absSender, Long chatId, SendMessage message) {
        message.enableHtml(true);
        message.setChatId(chatId.toString());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
