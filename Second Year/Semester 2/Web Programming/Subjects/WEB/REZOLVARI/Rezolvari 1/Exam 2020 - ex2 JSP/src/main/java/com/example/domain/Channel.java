package com.example.domain;

import java.util.List;

public class Channel {

    private int id;
    private int ownerId;
    private String name;
    private String descr;
    //private String subscribers;
    private List<Subscription> subscribers;

    public Channel(int i, int o, String n, String d, List<Subscription> s){
        this.id = i;
        this.ownerId = o;
        this.name = n;
        this.descr = d;
        this.subscribers = s;
    }

    public Channel( int o, String n, String d, List<Subscription> s){
        this.ownerId = o;
        this.name = n;
        this.descr = d;
        this.subscribers = s;
    }

    public int getId(){
        return this.id;
    }

    public int getOwnerId(){
        return this.ownerId;
    }

    public String getName(){
        return this.name;
    }

    public String getDescr(){
        return this.descr;
    }

    public  List<Subscription>  getSubscribers(){
        return this.subscribers;
    }
}
