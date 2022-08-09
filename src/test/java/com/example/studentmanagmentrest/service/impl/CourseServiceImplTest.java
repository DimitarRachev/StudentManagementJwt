package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.model.dto.CourseDto;
import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.repository.CourseRepository;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseServiceImplTest {
    CourseRepository mockedCourseRepository;
    TeacherRepository mockedTeacherRepository;
    StudentRepository mockedStudentRepository;
    CourseService courseService;

    @BeforeEach
    void setup() {
        mockedCourseRepository = mock(CourseRepository.class);
        mockedTeacherRepository = mock(TeacherRepository.class);
        mockedStudentRepository = mock(StudentRepository.class);
        courseService = new CourseServiceImpl(mockedCourseRepository, mockedTeacherRepository, mockedStudentRepository);
    }

    @Test
    void addTeacher() {
        when(mockedCourseRepository.findByNameAndDeletedFalse("existent")).thenReturn(new Course());
        when(mockedCourseRepository.findByNameAndDeletedFalse("nonExistent")).thenReturn(null);
        when(mockedTeacherRepository.findByNameAndDeletedFalse("existent")).thenReturn(new Teacher());
        when(mockedTeacherRepository.findByNameAndDeletedFalse("nonExistent")).thenReturn(null);
        assertAll(
                () -> assertEquals("Teacher nonExistent cannot be found in the database.",
                        courseService.addTeacher("nonExistent", "existent")),
                () -> assertEquals("Course nonExistent cannot be found in the database.",
                        courseService.addTeacher("existent", "nonExistent")),
                () -> assertEquals("Teacher existent now in charge for course existent",
                        courseService.addTeacher("existent", "existent"))
        );
    }

    @Test
    void addStudent() {
        Student student = new Student("name", 15);
        Course course = new Course("name", 45);
        course.setStudents(Set.of(student, new Student()));
        when(mockedCourseRepository.findByNameAndDeletedFalse("existent")).thenReturn(new Course());
        when(mockedCourseRepository.findByNameAndDeletedFalse("nonExistent")).thenReturn(null);
        when(mockedCourseRepository.findByNameAndDeletedFalse("stubed")).thenReturn(course);
        when(mockedStudentRepository.findByNameAndDeletedFalse("existent")).thenReturn(new Student());
        when(mockedStudentRepository.findByNameAndDeletedFalse("nonExistent")).thenReturn(null);
        when(mockedStudentRepository.findByNameAndDeletedFalse("stubed")).thenReturn(student);

        assertAll(
                () -> assertEquals("Student nonExistent cannot be found in the database.",
                        courseService.addStudent("nonExistent", "existent")),
                () -> assertEquals("Course nonExistent cannot be found in the database.",
                        courseService.addStudent("existent", "nonExistent")),
                () -> assertEquals("Student stubed is already enrolled in course stubed.",
                        courseService.addStudent("stubed", "stubed")),
                () -> assertEquals(" Student existent successfully enrolled in course existent.",
                        courseService.addStudent("existent", "existent"))
        );
    }

    @Test
    void add() {
        when(mockedCourseRepository.existsByNameAndDeletedFalse("exist")).thenReturn(true);
        when(mockedCourseRepository.existsByNameAndDeletedFalse("nonExist")).thenReturn(false);
        assertAll(
                () -> assertEquals("Course exist already in database.",
                        courseService.add("exist", 45)),
                () -> assertEquals("Course nonExist saved successfully.",
                        courseService.add("nonExist", 45))
        );
    }

    @Test
    void updateDuration() {
        when(mockedCourseRepository.existsByNameAndDeletedFalse("exist")).thenReturn(true);
        when(mockedCourseRepository.existsByNameAndDeletedFalse("nonExist")).thenReturn(false);
        assertAll(
                () -> assertEquals("exist's duration changed to 45 hours.",
                        courseService.updateDuration("exist", 45)),
                () -> assertEquals("Course nonExist cannot be found in the database.",
                        courseService.updateDuration("nonExist", 45))
        );
    }

    @Test
    void removeCourseByName() {
        when(mockedCourseRepository.findByNameAndDeletedFalse("deleted")).thenReturn(null);
        when(mockedCourseRepository.findByNameAndDeletedFalse("exist")).thenReturn(new Course());
        assertAll(
                ()-> assertEquals("Course deleted cannot be found in the database.",
                        courseService.removeCourseByName("deleted")),
                ()-> assertEquals("Course exist successfully removed from the database.",
                        courseService.removeCourseByName("exist"))
        );

    }

    @Test
    void makeCoursesDto() {
        List<Course> courses = List.of(new Course("name1", 15), new Course("name2", 45));
        CourseDto dto1 = new CourseDto();
        dto1.setName("name1");
        CourseDto dto2 = new CourseDto();
        dto2.setName("name2");
        List<CourseDto> dtos = List.of(dto1, dto2);
        when(mockedCourseRepository.getAllByDeletedFalse()).thenReturn(courses);
        assertArrayEquals(dtos.toArray(), courseService.makeCoursesDto().toArray());

    }
}