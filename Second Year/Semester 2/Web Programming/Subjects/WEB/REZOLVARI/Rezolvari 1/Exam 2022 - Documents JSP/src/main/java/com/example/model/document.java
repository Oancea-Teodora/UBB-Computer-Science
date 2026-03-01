package com.example.model;

public class document {

    public int id;
    public String title;
    public String listOfTemplates;

    public document(int i, String t, String l){
        this.id = i;
        this.title = t;
        this.listOfTemplates = l;
    }

    public int getId(){
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    public String getListOfTemplates(){
        return this.listOfTemplates;
    }
}
