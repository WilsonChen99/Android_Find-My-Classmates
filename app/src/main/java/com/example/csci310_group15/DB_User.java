package com.example.csci310_group15;

public class DB_User {
    private String name;
    private String standing;
    public DB_User(){}
    public DB_User(String name, String standing) {
        this.name = name;
        this.standing = standing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStanding() {
        return standing;
    }

    public void setStanding(String standing) {
        this.standing = standing;
    }
}
