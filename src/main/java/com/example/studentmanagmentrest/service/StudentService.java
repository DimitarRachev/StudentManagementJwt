package com.example.studentmanagmentrest.service;



import com.example.studentmanagmentrest.model.dto.StudentDto;
import com.example.studentmanagmentrest.model.dto.StudentWithAgeDto;
import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Student;

import java.util.List;
import java.util.Set;

public interface StudentService {

    void save(Student student);

    String getAvgForStudent(String name);

    Student getStudentByName(String name);

    List<Student> getAll();

    Student findByName(String name);

    List<Student> getAllByCourse(Course course);

    String updateAge(String name, int newAge);

    String add(String name, int age);

    boolean studentExists(String name);

    String removeStudentByName(String name);

    boolean purge();

    Set<Student> getForDeletion();

    List<StudentDto> getAllDto();

    List<StudentWithAgeDto> getAllWithAge();
}
