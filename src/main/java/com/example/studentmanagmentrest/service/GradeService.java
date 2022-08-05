package com.example.studentmanagmentrest.service;



import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;

import java.util.List;

public interface GradeService  {
    void save(Grade grade);

    List<Grade> findByStudentAndCourse(Student student, Course course);

    List<Grade> getAllByStudent(Student student);

    List<Grade> findByStudent(Student student);

    List<Grade> getAllByCourse(Course course);

    String addGrade(String studentName, String courseName, double grade);

    void updateDeletedByCourse(Course course);

    void updateDeletedByStudent(Student studentByName);

    boolean purge();

    void deleteByStudents(Student student);
}
