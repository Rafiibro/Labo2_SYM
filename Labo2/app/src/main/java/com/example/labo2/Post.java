package com.example.labo2;

/* Definition d'un Post, utilisé dans GraphQL */
public class Post {
    private int id;
    private String title;
    private String description;
    private String content;
    private String date;

    public Post(int id, String title, String description, String content, String date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}
