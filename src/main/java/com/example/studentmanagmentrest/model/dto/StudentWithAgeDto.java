package com.example.studentmanagmentrest.model.dto;

public class StudentWithAgeDto {
    private String name;
    private int age;

    public StudentWithAgeDto() {
    }

    public StudentWithAgeDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public StudentWithAgeDto setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public StudentWithAgeDto setAge(int age) {
        this.age = age;
        return this;
    }
}
