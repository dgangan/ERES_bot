package com.eres;

import com.eres.common.OracleDBCommon;
import com.eres.parser.ERESParser;
import com.eres.parser.OracleDBUtil;
import com.eres.parser.OutageDay;
import org.jsoup.HttpStatusException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OutagesDBUpdater implements Runnable {

    public void run() {
        String urlTemplate = "https://eres.md/abon/planotkl/3-planotkl/%d}";
        int outagesCount = 0;
        int linkCount = 0;
        List<String> links = new ArrayList<>();
        int lastId = 0;
        OracleDBUtil dbu = null;
        ERESParser parser = new ERESParser();
        try{
        links = parser.getOutageLinks();
        dbu = new OracleDBUtil(OracleDBCommon.getConnection());
        lastId = dbu.getLastId();}
        catch (Exception e){e.printStackTrace();}
        for (String link : links) {
            try {
                URL url = new URL(link);
                OutageDay day = parser.parseOutagePage(url);
                System.out.println(day.getLink());
                linkCount++;
                if (day.getId() > lastId) {
                    dbu.writeOutageDay(day.getId(), day.getLdt(), day.getLink(), day.getAddressesToJsonString());
                    outagesCount++;
                }
            } catch (HttpStatusException e) {
                System.out.println("HTTP: 404");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(linkCount + " links fetched \nDB updated with " + outagesCount + " rows");
    }
}
