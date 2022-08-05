package com.example.studentmanagmentrest.service.impl;


import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.repository.GradeRepository;
import com.example.studentmanagmentrest.service.CourseService;
import com.example.studentmanagmentrest.service.GradeService;
import com.example.studentmanagmentrest.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    @Autowired
    public GradeServiceImpl(GradeRepository gradeRepository, StudentService studentService, CourseService courseService) {
        this.gradeRepository = gradeRepository;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @Override
    public void save(Grade grade) {
        gradeRepository.save(grade);
    }

    @Override
    public List<Grade> findByStudentAndCourse(Student student, Course course) {
        return gradeRepository.findByStudentAndCourse(student, course);
    }

    @Override
    public List<Grade> getAllByStudent(Student student) {
        return gradeRepository.getAllByStudentAndDeletedFalse(student);
    }

    @Override
    public List<Grade> findByStudent(Student student) {
        return gradeRepository.findByStudent(student);
    }

    @Override
    public List<Grade> getAllByCourse(Course course) {
        return gradeRepository.getAllByCourse(course);
    }

    @Override
    public String addGrade(String studentName, String courseName, double grade) {
        Student student = studentService.getStudentByName(studentName);
        if (student == null) {
            return "Student " + studentName + " cannot be found in the database.";
        }
        Course course = courseService.findByName(courseName);
        if (course == null) {
            return "Course " + courseName + " cannot be found in the database.";
        }
        gradeRepository.save(new Grade(student, course, grade));

        return String.format("Student %s get %.2f on %s", studentName, grade, courseName);
    }

    @Override
    public void updateDeletedByCourse(Course course) {
        gradeRepository.updateDeletedByCourse(course);
    }

    @Override
    public void updateDeletedByStudent(Student studentByName) {
    }

    @Override
    public boolean purge() {
        gradeRepository.deleteAllByDeletedTrue();
        return true;
    }

    @Override
    public void deleteByStudents(Student student) {
        gradeRepository.deleteByStudent(student);
    }
}
