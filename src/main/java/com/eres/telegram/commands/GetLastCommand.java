package com.eres.telegram.commands;

import com.eres.common.OracleDBCommon;
import com.eres.parser.OracleDBUtil;
import com.eres.parser.OutageDay;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class GetLastCommand extends AbstractCommand{

    public GetLastCommand(String identifier, String description){
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender,User user, Chat chat, String[] strings){
        String answer;

        try {
            OracleDBUtil dbu = new OracleDBUtil(OracleDBCommon.getConnection());
            OutageDay out = dbu.getOutageDayById(dbu.getLastId()).get();
            answer = out.getLdt().toString() + "\n" + out.getPrettyPrintString("","","");
        }catch (Exception e)
        {
            e.printStackTrace();
            answer = "Issues while getting last ID from DB";
        }
        sendTextAnswer(absSender, chat.getId(), answer);
    }


}
