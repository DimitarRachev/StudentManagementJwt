package com.example.studentmanagmentrest.model.dto;

public class TeacherDto {
    private String name;
    private String degree;

    public TeacherDto() {
    }

    public TeacherDto(String name, String degree) {
        this.name = name;
        this.degree = degree;
    }

    public String getName() {
        return name;
    }

    public TeacherDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDegree() {
        return degree;
    }

    public TeacherDto setDegree(String degree) {
        this.degree = degree;
        return this;
    }
}
