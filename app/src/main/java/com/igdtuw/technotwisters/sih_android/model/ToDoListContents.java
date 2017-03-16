package com.igdtuw.technotwisters.sih_android.model;

import java.io.Serializable;

public class ToDoListContents implements Serializable {
    private int id;
    String title;
    String date;
    String content;
    int color;
    private static int count = 0;

    public ToDoListContents(String title, String date, String content, int color){
        this.title = title;
        this.date = date;
        this.content = content;
        this.id = ++count;
        this.color = color;
    }

    public void setID(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public int getColor() {
        return color;
    }

    public static int getCount() {
        return count;
    }
}
