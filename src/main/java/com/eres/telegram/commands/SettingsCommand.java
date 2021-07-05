package com.eres.telegram.commands;

import com.eres.telegram.common.Users;
import com.eres.telegram.fsm.Regions;
import com.eres.telegram.fsm.SettingsStates;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

public class SettingsCommand extends AbstractCommand{

    public SettingsCommand(String identifier, String description){
        super(identifier, description);
    }
    public static SendMessage mainSettingsPage(){
        List<InlineKeyboardButton> settingsButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> settingsButtonsRow2 = new ArrayList<>();
        List<InlineKeyboardButton> settingsButtonsRow3 = new ArrayList<>();
        List<InlineKeyboardButton> settingsButtonsRow4 = new ArrayList<>();
        List<InlineKeyboardButton> settingsButtonsRow5 = new ArrayList<>();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton regionButton = new InlineKeyboardButton();
        InlineKeyboardButton cityButton = new InlineKeyboardButton();
        InlineKeyboardButton streetButton = new InlineKeyboardButton();
        InlineKeyboardButton resetButton = new InlineKeyboardButton();
        InlineKeyboardButton closeButton = new InlineKeyboardButton();
        regionButton.setText("\u2709 Регион");
        regionButton.setCallbackData("region_settings");
        cityButton.setText("\u2709 Населённый пункт");
        cityButton.setCallbackData("city_settings");
        streetButton.setText("\u2709 Улица");
        streetButton.setCallbackData("street_settings");
        resetButton.setText("\uD83D\uDD04 Сбросить настройки");
        resetButton.setCallbackData("reset_settings");
        closeButton.setText("\u2716 Закрыть");
        closeButton.setCallbackData("close");
        settingsButtonsRow1.add(regionButton);
        settingsButtonsRow2.add(cityButton);
        settingsButtonsRow3.add(streetButton);
        settingsButtonsRow4.add(resetButton);
        settingsButtonsRow5.add(closeButton);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(settingsButtonsRow1);
        rowList.add(settingsButtonsRow2);
        rowList.add(settingsButtonsRow3);
        rowList.add(settingsButtonsRow4);
        rowList.add(settingsButtonsRow5);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage message = new SendMessage();
        message.setText("\u2699 <b>Настройки:</b>\n\nВыберите настройку которую хотите изменить:");
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public static SendMessage regionSettingsPage(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(Regions region : Regions.values()){
            List<InlineKeyboardButton> settingsButtonsRow = new ArrayList<>();
            InlineKeyboardButton regionButton = new InlineKeyboardButton();
            regionButton.setText(region.name);
            regionButton.setCallbackData(region.toString());
            settingsButtonsRow.add(regionButton);
            rowList.add(settingsButtonsRow);

        }
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("back");
        List<InlineKeyboardButton> backButtonsRow = new ArrayList<>();
        backButtonsRow.add(backButton);
        rowList.add(backButtonsRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage message = new SendMessage();
        message.setText("\u2699 <b>Регион:</b>\n\nВыберите ваш решион:");
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }
    @Override
    public void execute(AbsSender absSender,User user, Chat chat, String[] strings){
        sendAnswer(absSender, chat.getId(), this.mainSettingsPage());
        Users.setState(chat.getId(), SettingsStates.SETTINGS_MAIN);
    }


}
