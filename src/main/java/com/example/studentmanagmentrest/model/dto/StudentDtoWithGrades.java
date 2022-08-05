package com.example.studentmanagmentrest.model.dto;

import java.util.List;

public class StudentDtoWithGrades  {
    private String name;
//    private List<Grade> grades;
    private List<Double> grades;

    public StudentDtoWithGrades(String name, List<Double> grades) {
        this.name = name;
        this.grades = grades;
    }

    public StudentDtoWithGrades() {}

    public void setName(String name) {
        this.name = name;
    }

    public void setGrades(List<Double> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return name + " -> " + String.format("%.2f",  grades.stream().mapToDouble(e -> e).average().orElse(0));
    }

    public List<Double> getGrades() {
        return grades;
    }

    public String getName() {
        return name;
    }
}
