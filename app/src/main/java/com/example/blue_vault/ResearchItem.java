package com.example.blue_vault;

public class ResearchItem {
    private String title;
    private String author;
    private String course;
    private String date;

    public ResearchItem(String title, String author, String course, String date) {
        this.title = title;
        this.author = author;
        this.course = course;
        this.date = date;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCourse() { return course; }
    public String getDate() { return date; }
}