package com.example.model;

public class Article {
    // id, user, journalid, summary, date
    public int id;
    public String user;
    public int journalid;
    public String summary;
    public int date;

    public Article(int i, String u, int j, String s, int d){
        this.id = i;
        this.user = u;
        this.date = d;
        this.summary = s;
        this.journalid = j;
    }

    public int getId(){
        return this.id;
    }

    public int getJournalid(){
        return this.journalid;
    }

    public int getDate(){
        return date;
    }

    public String getSummary(){
        return this.summary;
    }

    public String getUser(){
        return this.user;
    }
}
