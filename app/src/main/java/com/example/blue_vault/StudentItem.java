package com.example.blue_vault;

public class StudentItem {
    private String name;
    private String studentID;
    private String dept;

    public StudentItem(String name, String studentID, String dept) {
        this.name = name;
        this.studentID = studentID;
        this.dept = dept;
    }

    public String getName() { return name; }
    public String getStudentID() { return studentID; }
    public String getDept() { return dept; }
}