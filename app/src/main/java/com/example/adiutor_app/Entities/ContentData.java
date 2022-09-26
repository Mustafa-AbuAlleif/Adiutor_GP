package com.example.adiutor_app.Entities;

import android.widget.Button;

public class ContentData {

    public ContentData() {

    }

    String Name, Url, Type;
    int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    Button dwnFile;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Button getDwnFile() {
        return dwnFile;
    }

    public void setDwnFile(Button dwnFile) {
        this.dwnFile = dwnFile;
    }
}
