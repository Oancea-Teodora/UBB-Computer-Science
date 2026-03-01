package com.example.model;

public class Journal {

    private int id;
    private String name;

    public Journal(int i, String n){
        this.id = i;
        this.name = n;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
