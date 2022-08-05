package com.example.studentmanagmentrest.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Grade extends BaseEntity {

    @ManyToOne()
    private Student student;

    @ManyToOne
    private Course course;

    @Column
    private boolean deleted;

    @Column
    private double grade;

    public Grade() {
    }

    public Grade(Student student, Course course,double grade) {
        this.student = student;
        this.course = course;
        this.grade = grade;
        deleted = false;
    }

    public Student getStudent() {
        return student;
    }

    public boolean deleted() {
        return deleted;
    }

    public void changeDeleted() {
        deleted = !deleted;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
