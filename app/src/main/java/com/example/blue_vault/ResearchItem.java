package com.example.blue_vault;

import java.io.Serializable;

public class ResearchItem implements Serializable {
    private String title;
    private String author;
    private String school;
    private String course;
    private String date;
    private int status; // Approved, Declined, Pending
    private String researchAbstract;
    private String tags;
    private String doi;
    private float rating; 
    private boolean isPublished; // Added to distinguish between approved and actually visible to public

    public ResearchItem(String title, String author, String school, String course, String date) {
        this(title, author, school, course, date, 3, "", "", "", 0.0f, false);
    }

    public ResearchItem(String title, String author, String school, String course, String date, int status) {
        this(title, author, school, course, date, status, "", "", "", 0.0f, false);
    }

    public ResearchItem(String title, String author, String school, String course, String date, int status, String researchAbstract, String tags, String doi, float rating, boolean isPublished) {
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
        this.isPublished = isPublished;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSchool() { return school; }
    public String getCourse() { return course; }
    public String getDate() { return date; }
    public int getStatus() { return status; }
    public String getResearchAbstract() { return researchAbstract; }
    public String getTags() { return tags; }
    public String getDoi() { return doi; }
    public float getRating() { return rating; }
    public boolean isPublished() { return isPublished; }
    public void setPublished(boolean published) { isPublished = published; }
}