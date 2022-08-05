package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.repository.GradeRepository;
import com.example.studentmanagmentrest.service.CourseService;
import com.example.studentmanagmentrest.service.GradeService;
import com.example.studentmanagmentrest.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GradeServiceImplTest {
    StudentService mockedStudentService;
    CourseService mockedCourseService;
    GradeService gradeService;
    GradeRepository gradeRepository;

    @BeforeEach
    void setUp() {
        mockedCourseService=mock(CourseService.class);
        mockedStudentService = mock(StudentService.class);
        gradeRepository = mock(GradeRepository.class);
        gradeService = new GradeServiceImpl(gradeRepository, mockedStudentService, mockedCourseService);
    }


    @Test
    void addGrade() {
        when(mockedStudentService.getStudentByName("deleted")).thenReturn(null);
        when(mockedStudentService.getStudentByName("existing")).thenReturn(new Student("existing", 15));
        when(mockedCourseService.findByName("deleted")).thenReturn(null);
        when(mockedCourseService.findByName("existing")).thenReturn(new Course("existing", 45));
        assertAll(
                ()->assertEquals("Student deleted cannot be found in the database.",
                        gradeService.addGrade("deleted", "existing", 6.00)),
                ()->assertEquals("Course deleted cannot be found in the database.",
                        gradeService.addGrade("existing", "deleted", 6.00)),
                ()->assertEquals("Student existing get 6.00 on existing",
                        gradeService.addGrade("existing", "existing", 6.00))

        );
    }

}