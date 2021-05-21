package com.eres.telegram;

import com.eres.common.OracleDBCommon;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class MessageHandler {
    OracleDBBotUtil dbu;
    public MessageHandler() throws SQLException, IOException {
        this.dbu = new OracleDBBotUtil(OracleDBCommon.getConnection());
    }
    public String startCommand(Update update){
        String replyMsg;
        try{
            dbu.registerUser(update);
            replyMsg = "\u2705  Registration succeeded!";
        } catch (SQLIntegrityConstraintViolationException e) {
            replyMsg = "\u2757  Already registered, " + update.getMessage().getFrom().getFirstName() + ". Enjoy \u263A";
            e.printStackTrace();
        } catch(Exception e){
            replyMsg = "\u2757  Some unknown exception!";
            e.printStackTrace();
        }
        return replyMsg;
    }
}
