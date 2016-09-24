package com.util;


public enum Language {
    Hindi(0, "hindi"),
    Urdu(1, "Urdu"),
    English(3, "english"),
    Chinese(4, "chinese");

    private int id;
    private String language;

    Language(int id, String language) {
        this.id = id;
        this.language = language;
    }

    public int id() {
        return id;
    }

    public String language() {
        return language;
    }
}