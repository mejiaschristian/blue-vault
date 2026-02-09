package com.example.blue_vault;

import java.io.Serializable;

public class ResearchItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int rsID;
    private String idNumber;
    private String title;
    private String author;
    private String school;
    private String course;
    private String date;
    private int status;
    private String researchAbstract;
    private String tags;
    private String doi;
    private float rating;
    private String remarks;
    private boolean isPublished;

    public ResearchItem(int rsID, String idNumber, String title, String author, String school, String course,
                        String date, int status, String researchAbstract, String tags, String doi, float rating, String remarks, boolean isPublished) {
        this.rsID = rsID;
        this.idNumber = idNumber;
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
        this.remarks = remarks;
        this.isPublished = isPublished;
    }

    // Getters
    public int getRsID() { return rsID; }
    public String getIdNumber() { return idNumber; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSchool() { return school; }
    public String getCourse() { return course; }
    public String getDate() { return date; }
    public int getStatus() { return status; }
    public String getAbstract() { return researchAbstract; }
    public String getTags() { return tags; }
    public String getDoi() { return doi; }
    public float getRating() { return rating; }
    public String getRemarks() { return remarks; }
    public boolean isPublished() { return isPublished; }
}