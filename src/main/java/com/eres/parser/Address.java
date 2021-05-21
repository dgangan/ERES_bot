package com.eres.parser;

import java.time.LocalDate;

public class Address {

    private String region;
    private String city;
    private String street;
    private String details;

    public Address(){}
    public Address(String region, String city, String street, String details){
        this.region = region;
        this.city = city;
        this.street = street;
        this.details = details;
    }

    public String toString(){
        return "{" + "region=" + this.getRegion() + "," +
                "city=" + this.getCity() + "," +
                "street=" + this.getStreet() + ", " +
                "details=" + this.getDetails() +"}";
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


}
