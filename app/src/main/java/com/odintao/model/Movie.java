package com.odintao.model;

import java.util.ArrayList;

public class Movie {
    private String title, thumbnailUrl;



    private String objId;
    private String date;
    private String author;
    private ArrayList<String> categories;

    public Movie() {
    }
    public Movie(String name, String thumbnailUrl,
                 String date, String author,
                 ArrayList<String> categories) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.date = date;
        this.author = author;
        this.categories = categories;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String name) {
        this.title = name;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getGenre() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }
}