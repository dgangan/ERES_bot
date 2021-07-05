package com.eres.telegram.common;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class Utils {

    public static String getUserName(Message msg) {
        return getUserName(msg.getFrom());
    }

    public static String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    public static Long getChatId(Update update){
        if(update.hasCallbackQuery()){
            return update.getCallbackQuery().getMessage().getChatId();
        } else{
            return update.getMessage().getChatId();
        }
    }

    public static Integer getMessageId(Update update){
        if(update.hasCallbackQuery()){
            return update.getCallbackQuery().getMessage().getMessageId();
        } else{
            return update.getMessage().getMessageId();
        }
    }
}