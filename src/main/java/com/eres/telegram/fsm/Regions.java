package com.eres.telegram.fsm;

public enum Regions {
    KAMENKA("Каменский район"),
    RIBNITA("Рыбницкий район"),
    DUBOSSARI("Дубоссарский район"),
    GRIGORIOPOL("Григориопольский район"),
    SLOBODZEYA("Слободзейский район"),
    TIRASPOL("г.Тирасполь"),
    BENDERI("г.Бендеры");

    public String name;
    Regions(String name){
        this.name = name;
    }
}
