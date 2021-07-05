package com.eres.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.*;

public class ERESParser {

    Set<Address> addresses;
    String outageLinksPage = "https://eres.md/abon/planotkl/3-planotkl";
    static String regionRegexp = ".*район:|.*Тирасполь:\\W*|.*Бендеры:\\W*";
    static String cityAndDetailsRgx = ".*((?:с\\.|г\\.|пос\\.)\\W*?[\\p{L}\\p{N}-]*?),\\W*?(.*?)[;,,](.*)?";
    static String cityAloneRgx = "((?:с\\.|г\\.|пос\\.)\\W*?\\p{L}*?):\\W*";
    static String streetRgx = "(.*?)[,,;](.*)?";

    static Pattern cityAlonePtr = Pattern.compile(cityAloneRgx);
    static Pattern cityAndDetailsPtr = Pattern.compile(cityAndDetailsRgx);
    static Pattern streetPtr = Pattern.compile(streetRgx);

    private static String fieldCleanUp(String field){
        return field.split(":|;")[0].strip();
    }

    public List<String> getOutageLinks() throws IOException {
        Document doc = Jsoup.connect(outageLinksPage).get();
        return new ArrayList<>(doc.select("article").attr("class", "item").eachAttr("data-permalink"));
    }

    private Elements getOutageDocument(URL url) throws IOException {
        Document doc = Jsoup.connect(url.toString()).get();
        return  doc.select("article").attr("class","item").attr("data-permalink", url.toString())
                                                    .select("div").attr("class", "content clearfix")
                                                        .select("p")
                                                            .select("span").attr("style","text-decoration: underline;");
    }

    public OutageDay parseOutagePage(URL url) throws Exception{
        String link = url.toString();
        Integer linkId = Integer.valueOf(link.split("/")[6].split("-")[0]);
        LocalDate date = LocalDate.parse(link.substring(link.length()-15, link.length()-5));
        addresses = new HashSet<>();

        String currentRegion = "";      //Used to store temporary parts of address while iteration over elements
        String currentCity = "";
        String currentStreet = "";
        String currentDetails = "";
        Elements elements = this.getOutageDocument(url);

        for(Element e : elements){
            String fieldText = e.text();
            Matcher cityAndDetailsLine = cityAndDetailsPtr.matcher(fieldText);
            Matcher cityAloneMtc = cityAlonePtr.matcher(fieldText);
            Matcher streetMtc = streetPtr.matcher(fieldText);

            if(e.text().matches(regionRegexp)){
                currentRegion = fieldText;
                currentCity = fieldText.matches("(.*Тирасполь:\\W*)|(.*Бендеры:\\W*)") ? fieldText : "";
                currentStreet = currentDetails = "";  //Clearing city/street/details once get to new region

            } else if(cityAndDetailsLine.find()){
                currentCity = cityAndDetailsLine.group(1);
                currentStreet = cityAndDetailsLine.group(2);
                currentDetails = cityAndDetailsLine.group(3);

            } else if(cityAloneMtc.find()){
                currentCity = cityAloneMtc.group(1);
                currentStreet = currentDetails = "";  //Clearing street/details once getting to new city

            } else if(streetMtc.find()){
                currentStreet = streetMtc.group(1);
                currentDetails = streetMtc.group(2);
            }

            if(!currentCity.isEmpty() && !currentRegion.isEmpty() && !currentStreet.isEmpty()){ //If address is complete - creating Address object
                this.addresses.add(new Address(fieldCleanUp(currentRegion), fieldCleanUp(currentCity),
                                                fieldCleanUp(currentStreet), fieldCleanUp(currentDetails)));
            }
        }
        if (this.addresses.size() > 0) {
            return new OutageDay(addresses, date, linkId, link);}
        else {
            throw new IOException("Parsing failed: " + url);
        }
    }
}