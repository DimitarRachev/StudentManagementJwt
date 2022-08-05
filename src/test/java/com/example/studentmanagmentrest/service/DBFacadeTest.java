package com.example.studentmanagmentrest.service;

import com.example.studentmanagmentrest.model.dto.CourseDtoWithGrades;
import com.example.studentmanagmentrest.model.dto.StudentDtoAvgGrade;
import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DBFacadeTest {
    StudentService mockedStudentService;
    TeacherService mockedTeacherService;
    CourseService mockedCourseService;
    GradeService mockedGradeService;
    PasswordEncoder passwordEncoder;
    DBFacade dbFacade;

    @BeforeEach
    void setUp() {
        mockedStudentService = mock(StudentService.class);
        mockedTeacherService = mock(TeacherService.class);
        mockedCourseService = mock(CourseService.class);
        mockedGradeService = mock(GradeService.class);
        dbFacade = new DBFacade(mockedStudentService, mockedTeacherService, mockedCourseService, mockedGradeService, passwordEncoder);
    }


    @Test
    void getCoursesDtoWithGrades() {
        List<Course> courseList = List.of(new Course("Java", 100), new Course("C#", 200));
        when(mockedCourseService.findAll()).thenReturn(courseList);
        List<CourseDtoWithGrades> dto = new ArrayList<>();
        dto.add(new CourseDtoWithGrades("Java", new ArrayList<>()));
        dto.add(new CourseDtoWithGrades("C#", new ArrayList<>()));
        List<CourseDtoWithGrades> coursesDtoWithGrades = dbFacade.getCoursesDtoWithGrades();
        assertAll(
                () -> assertEquals(dto.get(0).toString(), coursesDtoWithGrades.get(0).toString()),
                () -> assertEquals(dto.get(1).toString(), coursesDtoWithGrades.get(1).toString())
        );
    }


    @Test
    void getAvgForStudentsInCourse() {
        Course course = new Course("Java", 100);
        Student student = new Student("Student", 15);
        course.getStudents().add(student);
        student.getCourses().add(course);
        Grade g1 = new Grade(student, course, 6);
        Grade g2 = new Grade(student, course, 4);
        student.setGrades(List.of(g1, g2));
        StudentDtoAvgGrade dtoAvgGrade = new StudentDtoAvgGrade();
        dtoAvgGrade.setName("Student");
        dtoAvgGrade.setAvgGrade(5.00);

        when(mockedCourseService.getByName("Java")).thenReturn(course);
        when(mockedGradeService.findByStudentAndCourse(student, course)).thenReturn(List.of(g1, g2));

        List<StudentDtoAvgGrade> student1 = dbFacade.getAvgForStudentsInCourse("Java");
        assertEquals(dtoAvgGrade.toString(), student1.get(0).toString());
    }

    @Test
    void removeTeacherByName() {
        when(mockedTeacherService.findByName("deleted")).thenReturn(null);
        assertEquals("Teacher deleted cannot be found in the database.",
                dbFacade.removeTeacherByName("deleted"));
    }

    @Test
    void removeStudentFromCourse() {
        Course deletedCourse = mock(Course.class);
        when(deletedCourse.deleted()).thenReturn(true);
        Student deletedStudent = mock(Student.class);
        when(deletedStudent.deleted()).thenReturn(true);
        Student existingStudent = new Student("existingStudent", 15);
        Course existingCourse = new Course("existingCourse", 200);
        when(mockedStudentService.findByName("nonExistingStudent")).thenReturn(null);
        when(mockedStudentService.findByName("deletedStudent")).thenReturn(deletedStudent);
        when(mockedStudentService.findByName("existingStudent")).thenReturn(existingStudent);
        when(mockedCourseService.findByName("nonExistingCourse")).thenReturn(null);
        when(mockedCourseService.findByName("deletedCourse")).thenReturn(deletedCourse);
        when(mockedCourseService.findByName("existingCourse")).thenReturn(existingCourse);
        assertAll(
                () -> assertEquals("Course nonExistingCourse cannot be found in the database.",
                        dbFacade.removeStudentFromCourse("existingStudent", "nonExistingCourse")),
                () -> assertEquals("Student existingStudent isn't enrolled in course null.",
                        dbFacade.removeStudentFromCourse("existingStudent", "deletedCourse")),
                () -> assertEquals("Student nonExistingStudent cannot be found in the database.",
                        dbFacade.removeStudentFromCourse("nonExistingStudent", "existingCourse")),
                () -> assertEquals("Student null isn't enrolled in course existingCourse.",
                        dbFacade.removeStudentFromCourse("deletedStudent", "existingCourse")),
                () -> assertEquals("Student existingStudent isn't enrolled in course existingCourse.",
                        dbFacade.removeStudentFromCourse("existingStudent", "existingCourse")),
                () -> {
                    existingCourse.getStudents().add(existingStudent);
                    assertEquals("Student existingStudent successfully removed from existingCourse course.",
                            dbFacade.removeStudentFromCourse("existingStudent", "existingCourse"));
                }
        );

    }
}