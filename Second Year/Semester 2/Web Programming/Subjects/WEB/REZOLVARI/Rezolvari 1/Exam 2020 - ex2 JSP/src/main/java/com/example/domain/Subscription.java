package com.example.domain;

public class Subscription {
    private String name;
    private int date;

    public Subscription(String n, int d){
        this.date = d;
        this.name = n;
    }

    public String getName(){
        return this.name;
    }

    public int getDate(){
        return this.date;
    }

    public void setDate(int d){
        this.date = d;
    }

    @Override
    public String toString(){
        return "User: " + this.name;
    }
}
