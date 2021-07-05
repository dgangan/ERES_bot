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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception{

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            Bot bot = Bot.getInstance();
            botsApi.registerBot(bot);
            System.out.println("Telegram NMS bot has started");
            bot.hello("181645724");
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleWithFixedDelay(new OutagesDBUpdater(), 1, 1, TimeUnit.MINUTES);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
//        OutageDay a2 = dbh.getOutageDayById(dbh.getLastId()).get();
//        System.out.println(a2.getLdt());
//        System.out.println(a2.getPrettyPrintString("","","",""));
//        List<OutageDay> outagesRange = dbu.getOutageDaysFromDate(LocalDate.of(2021,05,16));
//        System.out.println("Range:");
//        outagesRange.forEach(d -> System.out.println(d.getId() + ":" + d.getLdt() + ":" + d.getLink() +"\n<" + d.getPrettyPrintString("Рыбницкий район","с.Мокра","") + ">"));
    }

}