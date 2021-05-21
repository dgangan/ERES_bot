package com.eres.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.*;
import java.time.LocalDate;

public class OracleDBBotUtil {
    Connection conn;
    String TABLE_NAME = "users";
    public OracleDBBotUtil(Connection conn) {
        this.conn = conn;
    }

    public void registerUser(Update update) throws Exception {
        final String queryStatement = "INSERT INTO " + TABLE_NAME +
                " (user_id, user_name, user_first_name, user_last_name, user_role, chat_id, register_date)" +
                " VALUES (?,?,?,?,?,?,?)";

        System.out.println("\n Query is " + queryStatement);

        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(queryStatement)){
            ps.setLong(1,update.getMessage().getFrom().getId());
            ps.setString(2,update.getMessage().getFrom().getUserName());
            ps.setString(3,update.getMessage().getFrom().getFirstName());
            ps.setString(4,update.getMessage().getFrom().getLastName());
            ps.setString(5, "user");
            ps.setLong(6,update.getMessage().getChatId());
            ps.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
            conn.commit();

            System.out.println("User was registered " +  update.getMessage().getFrom().getUserName());
        } catch(SQLIntegrityConstraintViolationException e ){
            System.out.println("Registration failure  " +  update.getMessage().getFrom().getUserName());
            throw e;
        }
    }

}
