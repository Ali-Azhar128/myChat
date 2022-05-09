package com.example.mychat.models;

import java.io.Serializable;

public class User implements Serializable {

     public String name;
     public String email;
     public String profileImage;
     public String id;
    public User(){}

    public User( String name, String email, String profileImage) {

        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }

    public void setEmail(String email) {
        this.email = email;
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
