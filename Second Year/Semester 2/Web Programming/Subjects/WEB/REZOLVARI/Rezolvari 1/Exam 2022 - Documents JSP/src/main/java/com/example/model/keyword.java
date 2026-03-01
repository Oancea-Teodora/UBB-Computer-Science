package com.example.model;

public class keyword {

    private int id;
    private String key;
    private String value;

    public keyword(int i, String k, String v){
        this.id = i;
        this.key = k;
        this.value = v;
    }

    public int getId(){
        return this.id;
    }

    public String getValue(){
        return this.value;
    }

    public String getKey(){
        return this.key;
    }
}
