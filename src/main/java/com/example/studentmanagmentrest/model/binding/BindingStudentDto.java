package com.example.studentmanagmentrest.model.binding;

public class BindingStudentDto {
    public String name;
    public int age;


    public BindingStudentDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
