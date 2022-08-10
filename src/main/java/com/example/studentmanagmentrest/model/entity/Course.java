package com.example.studentmanagmentrest.model.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course extends NameEntity {

    @Column(name = "total_hours")
    private int totalHours;

    @Column
    private boolean deleted;

    @ManyToMany
    private Set<Student> students;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;


    public Course(String name, int totalHours) {
        this();
        super.name = name;
        this.totalHours = totalHours;
        deleted = false;
    }

    public Course() {
        super();
        students = new HashSet<>();
    }

    public boolean addStudent(Student student) {
        return students.add(student);
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public boolean deleted() {
        return deleted;
    }

    public void changeDeleted() {
        deleted = !deleted;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
