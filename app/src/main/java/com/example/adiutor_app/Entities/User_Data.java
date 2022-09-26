package com.example.adiutor_app.Entities;

import java.io.Serializable;

public class User_Data implements Serializable {
    public User_Data() {
    }

    private String name,Email,Password,ImgUrl;

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    private int ID,UserTypeID;

    public int getUserTypeID() {
        return UserTypeID;
    }

    public void setUserTypeID(int userTypeID) {
        UserTypeID = userTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

