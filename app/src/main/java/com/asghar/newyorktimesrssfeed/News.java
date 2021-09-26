package com.asghar.newyorktimesrssfeed;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class News implements Serializable {
    private String Title;
    private String Link;
    private String Desc;

    public void setTitle(String title) {
        Title = title;
    }

    public void setLink(String link) {
        Link = link;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getTitle() {
        return Title;
    }

    public String getLink() {
        return Link;
    }

    public String getDesc() {
        return Desc;
    }

    @NonNull
    @Override
    public String toString() {
        return "News: \n" +
                "Title= " + Title + '\n' +
                "Link= " + Link + '\n' +
                "Desc= " + Desc;
    }
}
