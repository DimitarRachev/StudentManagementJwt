package com.example.studentmanagmentrest.model.dto;


import com.example.studentmanagmentrest.model.entity.Teacher;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseDto {
    private String name;
    private Set<StudentDto> students;
    private Teacher teacher;

    public CourseDto(String name, Set<StudentDto> students, Teacher teacher) {
        this.name = name;
        this.students = students;
        this.teacher = teacher;
    }

    public CourseDto() {
        students = new LinkedHashSet<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudents(Set<StudentDto> students) {
        this.students = students;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course ").append(name).append(": ").append(System.lineSeparator());
        sb.append("  Teacher: ");
        if (teacher != null) {
            sb.append(teacher.getName()).append(System.lineSeparator());
        } else {
            sb.append("none assigned.").append(System.lineSeparator());
        }
        sb.append(students.stream().map(StudentDto::toString).map(s -> "    " + s).collect(Collectors.joining(System.lineSeparator())));
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public Set<StudentDto> getStudents() {
        return students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseDto courseDto = (CourseDto) o;
        return name.equals(courseDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

