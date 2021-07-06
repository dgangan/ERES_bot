package com.eres.telegram.handlers;

import com.eres.common.OracleDBCommon;
import com.eres.telegram.Bot;
import com.eres.telegram.BotDBUtil;
import com.eres.telegram.commands.SettingsCommand;
import com.eres.telegram.common.Users;
import com.eres.telegram.common.Utils;
import com.eres.telegram.fsm.IStates;
import com.eres.telegram.fsm.Regions;
import com.eres.telegram.fsm.SettingsStates;
import com.eres.telegram.fsm.StatesGroups;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

public class SettingsReplyHandler {

    public static void handle(Update update){
        System.out.println(Users.getState(Utils.getChatId(update)));
        if( getState(update) == SettingsStates.SETTINGS_MAIN) {
            if(update.hasCallbackQuery()) {
                if(getCallbackData(update).equals("region_settings")){
                    deleteMessage(update);
                    sendRegionPageSettings(update);
                    Users.setState(Utils.getChatId(update), SettingsStates.SETTINGS_REGION);
                }
                else if(getCallbackData(update).equals("city_settings")){
                    deleteMessage(update);
                    Users.setState(Utils.getChatId(update), SettingsStates.SETTINGS_CITY);
                    Bot.getInstance().setAnswer(Utils.getChatId(update), "\u2754 Введите название населенного пункта: ");
                }
                else if(getCallbackData(update).equals("street_settings")){
                    deleteMessage(update);
                    Users.setState(Utils.getChatId(update), SettingsStates.SETTINGS_STREET);
                    Bot.getInstance().setAnswer(Utils.getChatId(update), "\u2754 Введите назваие улицы: ");
                }
                else if(getCallbackData(update).equals("reset_settings")){
                    deleteMessage(update);
                    Bot.getInstance().setAnswer(Utils.getChatId(update), "\uD83D\uDD04 Настройки сброшены");
                    sendMainSettingPage(update);
                    try{
                        new BotDBUtil(OracleDBCommon.getConnection()).deleteUserSettings(Utils.getChatId(update));
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        if(getState(update) == SettingsStates.SETTINGS_REGION){
            if(update.hasCallbackQuery()) {
                if (Arrays.stream(Regions.values()).anyMatch(e -> e.name().equals(getCallbackData(update)))) {
                    Users.setPreviousState(Utils.getChatId(update));
                    String region = Regions.valueOf(getCallbackData(update)).name;
                    deleteMessage(update);
                    String answer = "\u2705 Выбран регион: " + region;
                    Bot.getInstance().setAnswer(Utils.getChatId(update), answer);
                    sendMainSettingPage(update);
                    try{
                        new BotDBUtil(OracleDBCommon.getConnection()).updateUserRegion(update, region);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
                if(getCallbackData(update).equals("back")){
                    Users.setPreviousState(Utils.getChatId(update));
                    deleteMessage(update);
                    sendMainSettingPage(update);
                }
            }
        }
        if( getState(update) == SettingsStates.SETTINGS_CITY) {
            if(update.hasMessage()) {
                Users.setPreviousState(Utils.getChatId(update));
                String city = update.getMessage().getText();
                String answer = "\u2705 Выбран населённый пункт: " + city;
                Bot.getInstance().setAnswer(Utils.getChatId(update), answer);
                sendMainSettingPage(update);
                try{
                    new BotDBUtil(OracleDBCommon.getConnection()).updateUserCity(update, city);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        if( getState(update) == SettingsStates.SETTINGS_STREET) {
            if(update.hasMessage()) {
                Users.setPreviousState(Utils.getChatId(update));
                String street = update.getMessage().getText();
                String answer = "\u2705 Выбрана улица: " + street;
                Bot.getInstance().setAnswer(Utils.getChatId(update), answer);
                sendMainSettingPage(update);
                try{
                    new BotDBUtil(OracleDBCommon.getConnection()).updateUserStreet(update, street);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        else if(getCallbackData(update).equals("close")){
            deleteMessage(update);
            Users.resetState(Utils.getChatId(update));
        }
    }

    private static StatesGroups getStateGroupByUpdate(Update update){
        return Users.getState(Utils.getChatId(update)).getStateGroup();
    }

    private static IStates getState(Update update){
        return Users.getState(Utils.getChatId(update));
    }

    private static String getCallbackData(Update update){
        return update.getCallbackQuery().getData();
    }

    private static void deleteMessage(Update update){

        Bot.getInstance().setAnswer(new DeleteMessage(Utils.getChatId(update).toString(), Utils.getMessageId(update)));
        String chatId = Utils.getChatId(update).toString();
    }

    public static void sendMainSettingPage(Update update){
        SendMessage message = SettingsCommand.mainSettingsPage();
        message.enableHtml(true);
        message.setChatId(Utils.getChatId(update).toString());
        Bot.getInstance().setAnswer(message);
    }

    private static void sendRegionPageSettings(Update update){
        SendMessage message = SettingsCommand.regionSettingsPage();
        message.enableHtml(true);
        message.setChatId(Utils.getChatId(update).toString());
        Bot.getInstance().setAnswer(message);
    }
}
