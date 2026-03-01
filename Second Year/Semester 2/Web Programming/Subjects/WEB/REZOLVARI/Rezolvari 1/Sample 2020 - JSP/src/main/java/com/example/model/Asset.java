package com.example.model;

public class Asset {
    // id, userid, name, descr, value
    private int id;
    private int userid;
    private String name;
    private String descr;
    private int value;

    public Asset(int i, int u, String n, String d, int v){
        this.id = i;
        this.userid = u;
        this.descr = d;
        this.value = v;
        this.name = n;
    }

    public String getName(){
        return this.name;
    }

    public String getDescr(){
        return this.descr;
    }

    public int getId(){
        return this.id;
    }

    public int getUserid(){
        return this.userid;
    }

    public int getValue(){
        return this.value;
    }

    @Override
    public String toString(){
        return "name: " + this.getName();
    }
}
