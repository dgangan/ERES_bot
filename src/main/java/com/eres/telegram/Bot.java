package com.eres.telegram;

import com.eres.common.OracleDBCommon;
import com.eres.telegram.commands.GetLastCommand;
import com.eres.telegram.commands.StartCommand;
import com.eres.telegram.commands.SettingsCommand;
import com.eres.telegram.common.Users;
import com.eres.telegram.common.Utils;
import com.eres.telegram.fsm.*;
import com.eres.telegram.handlers.SettingsReplyHandler;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

public class Bot extends TelegramLongPollingCommandBot  {

    private static Bot bot = null;

    private Bot() {
        super();
        register(new StartCommand("start", "Старт"));
        register(new GetLastCommand("getlast", "Get Last"));
        register(new SettingsCommand("settings", "Settings"));
    }

    public static Bot getInstance(){
        if (bot == null) return new Bot();
        return bot;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if(getStateGroupByUpdate(update) == StatesGroups.INIT) {}
        else if(getStateGroupByUpdate(update) == StatesGroups.SETTINGS) {
            System.out.println("Settings section");
            SettingsReplyHandler.handle(update);
        }
    }

    private static StatesGroups getStateGroupByUpdate(Update update){
        return Users.getState(Utils.getChatId(update)).getStateGroup();
    }

    public void setAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        answer.enableHtml(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setAnswer(BotApiMethod<?> botApiMethod) {
        try {
            execute(botApiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void hello(String chatId){
        SendMessage answer = new SendMessage();
        answer.setText("helloo");
        answer.setChatId(chatId);
        answer.enableHtml(true);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
