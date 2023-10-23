package com.example.csci310_group15;

public class User {
    private String name = null;
    private String email = null;
    private String standing = null;
    private String uri = null;
    private String uid = null;
    private String uscID = null;

    public User() {

    }

    public User(String name, String email, String standing, String uri, String uscID, String uid) {
        this.name = name;
        this.email= email;
        this.standing = standing;
        this.uri = uri;
        this.uscID = uscID;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStanding() {
        return standing;
    }

    public void setStanding(String standing) {
        this.standing = standing;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUscID() {
        return uscID;
    }

    public void setUscID(String uscID) {
        this.uscID = uscID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
