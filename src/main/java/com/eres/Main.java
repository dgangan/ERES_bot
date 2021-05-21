package com.eres;

import com.eres.common.OracleDBCommon;
import com.eres.parser.ERESParser;
import com.eres.parser.OracleDBUtil;
import com.eres.parser.OutageDay;
import com.eres.telegram.Bot;
import org.jsoup.HttpStatusException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
            System.out.println("Telegram NMS bot has started");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
//        String urlTemplate = "https://eres.md/abon/planotkl/3-planotkl/%d}";
//        ERESParser parser = new ERESParser();
//        List<String> links = parser.getOutageLinks();
//        OracleDBUtil dbu = new OracleDBUtil(dbh.getConnection());
//        int lastId = dbu.getLastId();
//        for(String link : links){
//            try{
//                URL url = new URL(link);
//                OutageDay day = parser.getOutageSetOfAddresses(url);
//                System.out.println(day.getLink());
//                if(day.getId()>lastId)
//                    dbu.writeOutageDay(day.getId(), day.getLdt(), day.getLink(), day.getAddressesToJsonString());
//            } catch (HttpStatusException e) {
//                    System.out.println("HTTP: 404");
//            } catch (IOException e) {
//                    System.out.println(e.getMessage());
//                }
//        }
//        OutageDay a2 = dbh.getOutageDayById(dbh.getLastId()).get();
//        System.out.println(a2.getLdt());
//        System.out.println(a2.getPrettyPrintString("","","",""));
//        List<OutageDay> outagesRange = dbu.getOutageDaysFromDate(LocalDate.of(2021,05,16));
//        System.out.println("Range:");
//        outagesRange.forEach(d -> System.out.println(d.getId() + ":" + d.getLdt() + ":" + d.getLink() +"\n<" + d.getPrettyPrintString("Рыбницкий район","с.Мокра","") + ">"));
    }
}