package com.example.studentmanagmentrest.model.dto;



public class StudentDtoAvgGrade {
    private String name;
    private double avgGrade;

    public StudentDtoAvgGrade() { }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgGrade(double avgGrade) {
        this.avgGrade = avgGrade;
    }
    @Override
    public String toString() {
        return name + "-> " + String.format("%.2f", avgGrade);
    }

    public String getName() {
        return name;
    }

    public double getAvgGrade() {
        return avgGrade;
    }
}
