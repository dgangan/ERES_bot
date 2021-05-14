package com.eres;

import org.jsoup.HttpStatusException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception{
        String urlTemplate = "https://eres.md/abon/planotkl/3-planotkl/%d}";
        ERESParser parser = new ERESParser();
        List<String> links = parser.getOutageLinks();
        DBHelper dbh = new DBHelper();
        int lastId = dbh.getLastId();
        for(String link : links){
            try{
                URL url = new URL(link);
                Adresses day = parser.getOutageSetOfAddresses(url);
                System.out.println(day.getLink());
                if(day.getId()>lastId)
                    dbh.writeOutageDay(day.getId(), day.getLdt(), day.getLink(), day.getAddressesToJsonString());
            } catch (HttpStatusException e) {
                    System.out.println("HTTP: 404");
            } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
        }
        Adresses a2 = dbh.getOutageDayById(7934).get();
        System.out.println(a2.getLdt());
        System.out.println(a2.getPrettyPrintString("","","",""));
    }
}