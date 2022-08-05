package com.example.studentmanagmentrest.model.binding;

public class TeacherBindingDto {
    private String name;
    private String degree;

    public TeacherBindingDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
