package com.example.model;

public class User {

    private int id;
    private String username;
    private String mother;
    private String father;

    public User(int i, String u, String m, String p){
        this.id = i;
        this.username = u;
        this.mother = m;
        this.father = p;
    }

    public User(String u, String m, String p){
        this.username = u;
        this.mother = m;
        this.father = p;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return this.username;
    }

    public String getMother(){
        return this.mother;
    }

    public String getFather(){
        return this.father;
    }
}
