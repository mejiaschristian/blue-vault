package com.example.blue_vault;

public class TeacherItem {
    private String name;
    private String teacherID;
    private String dept;

    public TeacherItem(String name, String teacherID, String dept) {
        this.name = name;
        this.teacherID = teacherID;
        this.dept = dept;
    }

    public String getName() { return name; }
    public String getTeacherID() { return teacherID; }
    public String getDept() { return dept; }
}