package com.eres.telegram;

import com.eres.telegram.common.Utils;
import com.eres.telegram.fsm.Regions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.*;
import java.time.LocalDate;

public class BotDBUtil {
    Connection conn;
    String USERS_TABLE_NAME = "users";
    String USER_SETTINGS_TABLE_NAME = "user_settings";

    public BotDBUtil(Connection conn) {
        this.conn = conn;
    }

    public void registerUser(User user) throws Exception {
        final String queryStatement = "INSERT INTO " + USERS_TABLE_NAME +
                " (user_id, user_name, user_first_name, user_last_name, user_role, chat_id, register_date)" +
                " VALUES (?,?,?,?,?,?,?)";

        System.out.println("\n Query is " + queryStatement);

        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(queryStatement)) {
            ps.setLong(1, user.getId());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, "user");
            ps.setLong(6, user.getId());
            ps.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
            conn.commit();

            System.out.println("User was registered " + user.getUserName());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Registration failure  " + user.getUserName());
            throw e;
        }
    }

    private void updateUserSetting(Update update, String fieldName, String fieldData) throws Exception {
        final String queryStatement = "MERGE INTO " + USER_SETTINGS_TABLE_NAME
                + " d USING (SELECT ? user_id, ? " + fieldName + " from dual) s ON ( d.user_id=s.user_id ) "
                + "WHEN MATCHED THEN UPDATE SET d." + fieldName + "=s." + fieldName
                + " WHEN NOT MATCHED THEN INSERT (user_id," + fieldName + ") VALUES ( s.user_id, s." + fieldName + ")";


        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(queryStatement)) {
            ps.setLong(1, Utils.getChatId(update));
            ps.setString(2, fieldData);
            ps.executeUpdate();
            conn.commit();

            System.out.println("Filed was updated: " + Utils.getChatId(update) + "(" + fieldName + ":" + fieldData + ")");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Filed update failure  " + Utils.getChatId(update) + "(" + fieldName + ":" + fieldData + ")");
            throw e;
        }
    }

    public void deleteUserSettings(Long chatId) throws Exception {
        final String queryStatement = "DELETE FROM " + USER_SETTINGS_TABLE_NAME +
                " WHERE USER_ID = ?";

        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(queryStatement)) {
            ps.setLong(1, chatId);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Setting reset: success");
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Setting reset: failure");
            throw e;
        }
    }

    public void updateUserRegion(Update update, String fieldData) throws Exception {
        this.updateUserSetting(update, "address_region", fieldData);
    }

    public void updateUserCity(Update update, String fieldData) throws Exception {
        this.updateUserSetting(update, "address_city", fieldData);
    }

    public void updateUserStreet(Update update, String fieldData) throws Exception {
        this.updateUserSetting(update, "address_street", fieldData);
    }

    public void getUserSettings(User user) throws Exception {
        final String queryStatement = "SELECT FROM " + USERS_TABLE_NAME +
                " WHERE " +
                " VALUES (?,?,?,?,?,?,?)";

        System.out.println("\n Query is " + queryStatement);

        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(queryStatement)) {
            ps.setLong(1, user.getId());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, "user");
            ps.setLong(6, user.getId());
            ps.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
            ps.executeUpdate();
            conn.commit();

            System.out.println("User was registered " + user.getUserName());
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Registration failure  " + user.getUserName());
            throw e;
        }
    }
}

//    public List<Integer> getAllUsersIDs(){
//        final String queryStatement = "SELECT user_id FROM " + TABLE_NAME;
//    }

