package com.example.blue_vault;

public class ResearchItem {
    private String title;
    private String author;
    private String school;
    private String course;
    private String date;
    private String status; // Added for Super Admin view (Approved/Declined)

    public ResearchItem(String title, String author, String school, String course, String date) {
        this.title = title;
        this.author = author;
        this.school = school;
        this.course = course;
        this.date = date;
        this.status = "Pending"; // Default
    }

    public ResearchItem(String title, String author, String school, String course, String date, String status) {
        this.title = title;
        this.author = author;
        this.school = school;
        this.course = course;
        this.date = date;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSchool() { return school; }
    public String getCourse() { return course; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}