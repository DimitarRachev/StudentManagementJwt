package com.example.studentmanagmentrest.model.binding;

public class GradeBindingDto {
    private String courseName;
    private String studentName;
    private double grade;

    public GradeBindingDto() {
    }

    public String getCourseName() {
        return courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public double getGrade() {
        return grade;
    }

    public GradeBindingDto setCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public GradeBindingDto setStudentName(String studentName) {
        this.studentName = studentName;
        return this;
    }

    public GradeBindingDto setGrade(double grade) {
        this.grade = grade;
        return this;
    }
}
