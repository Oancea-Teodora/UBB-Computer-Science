package com.example.model;

public class Template {
    public int id;
    public String name;
    public String textContent;

    public Template(int i, String t, String l){
        this.id = i;
        this.name = t;
        this.textContent = l;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){return this.name;}

    public String getTextContent(){
        return this.textContent;
    }
}
