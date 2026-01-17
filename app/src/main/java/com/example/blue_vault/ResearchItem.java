package com.example.blue_vault;

import java.io.Serializable;

public class ResearchItem implements Serializable {
    private String title;
    private String author;
    private String school;
    private String course;
    private String date;
    private String status;
    private String researchAbstract;
    private String tags;
    private String doi;
    private int rating; // Added rating field (0-5)

    public ResearchItem(String title, String author, String school, String course, String date) {
        this(title, author, school, course, date, "Pending", "", "", "", 0);
    }

    public ResearchItem(String title, String author, String school, String course, String date, String status) {
        this(title, author, school, course, date, status, "", "", "", 0);
    }

    public ResearchItem(String title, String author, String school, String course, String date, String status, String researchAbstract, String tags, String doi, int rating) {
        this.title = title;
        this.author = author;
        this.school = school;
        this.course = course;
        this.date = date;
        this.status = status;
        this.researchAbstract = researchAbstract;
        this.tags = tags;
        this.doi = doi;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSchool() { return school; }
    public String getCourse() { return course; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getResearchAbstract() { return researchAbstract; }
    public String getTags() { return tags; }
    public String getDoi() { return doi; }
    public int getRating() { return rating; }
}