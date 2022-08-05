package com.example.studentmanagmentrest.model.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDtoWithGrades  {
    private String name;
    private List<StudentDtoWithGrades> students;

    public CourseDtoWithGrades(String name, List<StudentDtoWithGrades> students) {
        this.name = name;
        this.students = students;
    }

    public CourseDtoWithGrades() { students = new ArrayList<>(); }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudents(List<StudentDtoWithGrades> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course ").append(name).append(": ").append(System.lineSeparator());
        sb.append(students.stream().map(StudentDtoWithGrades::toString).map(s -> "  " + s).collect(Collectors.joining(System.lineSeparator())));
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public List<StudentDtoWithGrades> getStudents() {
        return students;
    }
}
