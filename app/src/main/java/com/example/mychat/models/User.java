package com.example.mychat.models;

public class User {

    private String name;
    private String email;
    private String profileImage;
    public User(){}

    public User( String name, String email, String profileImage) {

        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }



    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImage() {
        return profileImage;
    }



    public void setName(String name) {
        this.name = name;
    }



    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }




}
