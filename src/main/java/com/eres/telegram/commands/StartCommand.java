package com.eres.telegram.commands;

import com.eres.common.OracleDBCommon;
import com.eres.telegram.BotDBUtil;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends AbstractCommand{

    public StartCommand(String identifier, String description){
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings){
        try{
            new BotDBUtil(OracleDBCommon.getConnection()).registerUser(user);
        } catch(Exception e){}

        sendTextAnswer(absSender, chat.getId(), "It's working");

    }
}
