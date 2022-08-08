package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.repository.GradeRepository;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentServiceImplTest {
    StudentRepository mockedStudentRepository;
    GradeRepository mockedGradeRepository;
    StudentService studentService;
    ModelMapper mapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        mockedGradeRepository = mock(GradeRepository.class);
        mockedStudentRepository = mock(StudentRepository.class);
        studentService = new StudentServiceImpl(mockedStudentRepository, mockedGradeRepository, mapper);
    }

    @Test
    void getAvgForStudent() {
        Student student = new Student("existing", 15);
        List<Grade> grades = List.of(new Grade(new Student(), new Course(), 6), new Grade(new Student(), new Course(), 4));
        when(mockedStudentRepository.getStudentByNameAndDeletedFalse("deleted")).thenReturn(null);
        when(mockedStudentRepository.getStudentByNameAndDeletedFalse("existing")).thenReturn(student);
        when(mockedGradeRepository.getAllByStudentAndDeletedFalse(student)).thenReturn(grades);
        assertAll(
                () -> assertEquals("Student deleted cannot be found in the database.",
                        studentService.getAvgForStudent("deleted")),
                () -> assertEquals("existing's average grade is: 5.00",
                        studentService.getAvgForStudent("existing"))
        );

    }
    
    @Test
    void updateAge() {
        when(mockedStudentRepository.existsByNameAndDeletedFalse("deleted")).thenReturn(false);
        when(mockedStudentRepository.existsByNameAndDeletedFalse("existing")).thenReturn(true);
        assertAll(
                () -> assertEquals("Student deleted cannot be found in the database.",
                        studentService.updateAge("deleted", 15)),
                () -> assertEquals("existing's age updated to 15.",
                        studentService.updateAge("existing", 15))
        );
    }

    @Test
    void add() {
        when(mockedStudentRepository.existsByNameAndDeletedFalse("Notexisting")).thenReturn(false);
        when(mockedStudentRepository.existsByNameAndDeletedFalse("existing")).thenReturn(true);
        assertAll(
                () -> assertEquals("Student existing is already in database",
                        studentService.add("existing", 15)),
                () -> assertEquals("Student Notexisting successfully added in database",
                        studentService.add("Notexisting", 15))
        );
    }

    @Test
    void removeStudentByName() {
        when(mockedStudentRepository.findByNameAndDeletedFalse("deleted")).thenReturn(null);
        when(mockedStudentRepository.findByNameAndDeletedFalse("Pesho")).thenReturn(new Student("Pesho", 15));
        assertAll(
                () -> assertEquals("Student deleted cannot be found in the database.",
                       studentService.removeStudentByName("deleted") ),
                () -> assertEquals("Student Pesho successfully deleted from database",
                        studentService.removeStudentByName("Pesho"))
        );

    }
}