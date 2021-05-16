package com.eres;

import org.jsoup.HttpStatusException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        String urlTemplate = "https://eres.md/abon/planotkl/3-planotkl/%d}";
        ERESParser parser = new ERESParser();
        List<String> links = parser.getOutageLinks();
        DBHelper dbh = new DBHelper("./conf/oracle_db.properties");
        int lastId = dbh.getLastId();
        for(String link : links){
            try{
                URL url = new URL(link);
                OutageDay day = parser.getOutageSetOfAddresses(url);
                System.out.println(day.getLink());
                if(day.getId()>lastId)
                    dbh.writeOutageDay(day.getId(), day.getLdt(), day.getLink(), day.getAddressesToJsonString());
            } catch (HttpStatusException e) {
                    System.out.println("HTTP: 404");
            } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
        }
//        OutageDay a2 = dbh.getOutageDayById(dbh.getLastId()).get();
//        System.out.println(a2.getLdt());
//        System.out.println(a2.getPrettyPrintString("","","",""));
        List<OutageDay> outagesRange = dbh.getOutageDaysFromDate(LocalDate.of(2021,05,16));
        System.out.println("Range:");
        outagesRange.forEach(d -> System.out.println(d.getLink()));
    }
}