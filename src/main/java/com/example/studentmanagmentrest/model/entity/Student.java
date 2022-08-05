package com.example.studentmanagmentrest.model.entity;

import com.example.studentmanagmentrest.enumeration.UserRole;
import com.google.common.collect.Sets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Student extends UserEntity{

    @Column
    private int age;

    @Column
    private boolean deleted;

    @ManyToMany(mappedBy = "students")
     private Set<Course> courses;

    @OneToMany( mappedBy = "student", targetEntity = Grade.class)
    List<Grade> grades;

    public Student(String name, int age) {
        this();
        super.name = name;
        this.age = age;

    }

    public Student() {
        super();
        courses = new HashSet<>();
        grades = new ArrayList<>();
        deleted = false;
        setRoles(Sets.newHashSet(UserRole.STUDENT));
    }

    public Student(String name, int age, String username, String password) {
        this();
        this.name = name;
        this.age = age;
        setUsername(username);
        setPassword(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return this.name.equals(student.name) && this.getAge() == student.getAge();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }


    public boolean deleted() {
        return deleted;
    }

    public void changeDeleted() {
        deleted = !deleted;
    }

    public int getAge() {
        return age;
    }

    public Student setAge(int age) {
        this.age = age;
        return this;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Student setCourses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public Student setGrades(List<Grade> grades) {
        this.grades = grades;
        return this;
    }
}
