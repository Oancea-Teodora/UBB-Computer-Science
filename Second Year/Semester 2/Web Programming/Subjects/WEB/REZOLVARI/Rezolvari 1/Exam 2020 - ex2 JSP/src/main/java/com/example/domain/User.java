package com.example.domain;

public class User {

    private int id;
    private String name;
    private int age;
    private String gender;

    public User(int id, String n, int a, String g){
        this.id = id;
        this.name = n;
        this.age = a;
        this.gender = g;
    }

    public User(String n, int a, String g){
        this.name = n;
        this.age = a;
        this.gender = g;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getGender(){
        return this.gender;
    }

    public int getAge(){
        return this.age;
    }
}
