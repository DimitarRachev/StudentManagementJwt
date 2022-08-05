package com.example.studentmanagmentrest.model.binding;

public class CourseBindingDto {
    private String name;
    private int duration;

    public CourseBindingDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
