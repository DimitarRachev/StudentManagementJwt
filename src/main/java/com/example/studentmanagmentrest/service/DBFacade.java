package com.example.studentmanagmentrest.service;


import com.example.studentmanagmentrest.model.dto.StudentDto;
import com.example.studentmanagmentrest.utility.Message;
import com.example.studentmanagmentrest.utility.dbInitializer.Initializer;
import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.model.dto.CourseDto;
import com.example.studentmanagmentrest.model.dto.CourseDtoWithGrades;
import com.example.studentmanagmentrest.model.dto.StudentDtoAvgGrade;
import com.example.studentmanagmentrest.utility.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@ComponentScan(basePackages = {"com.example"})
@EntityScan(basePackages = {"com.example"})
@EnableJpaRepositories(basePackages = {"com.example"})
public class DBFacade {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final GradeService gradeService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public DBFacade(StudentService studentService, TeacherService teacherService, CourseService courseService, GradeService gradeService, PasswordEncoder passwordEncoder) {
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean makeDatabase() throws IOException {
        Initializer initializer = new Initializer(teacherService, studentService, courseService, gradeService, passwordEncoder);
        initializer.run();
        return true;
    }

    public String addNewTeacher(String name, String degree) {
        return teacherService.save(name, degree);
    }

    public String addNewCourse(String name, int hours) {
        return courseService.add(name, hours);
    }

    public String addNewStudent(String name, int age) {
        return studentService.add(name, age);
    }

    public String addGrade(String studentName, String courseName, double grade) {
        return gradeService.addGrade(studentName, courseName, grade);
    }

    public String addTeacherToCourse(String teacherName, String courseName) {
        return courseService.addTeacher(teacherName, courseName);
    }

    public String addStudentToCourse(String studentName, String courseName) {
        return courseService.addStudent(studentName, courseName);
    }

    public List<CourseDtoWithGrades> getCoursesDtoWithGrades() {
        List<Course> courses = courseService.findAll();
        return courses.stream().map(c -> DtoConverter.makeCourseDtoWithGrades(c, gradeService)).collect(Collectors.toList());
    }

    public List<CourseDto> getCoursesDto() {
        return courseService.makeCoursesDto();
    }

    public List<StudentDtoAvgGrade> getAvgForStudentsInCourse(String name) {
        Course course = courseService.getByName(name);
        if (course == null) {
            return null;
        }
        Set<Student> students = course.getStudents();
        if (students == null) {
            return null;
        }
        return students.stream().map(s -> DtoConverter.makeAvgGradeStudent(s, gradeService, course)).collect(Collectors.toList());
    }

    public String getAvgForStudent(String name) {
        return studentService.getAvgForStudent(name);
    }

    public String removeStudentByName(String name) {
        return studentService.removeStudentByName(name);
    }

    public String removeCourseByName(String name) {
        String toReturn = courseService.removeCourseByName(name);
        gradeService.updateDeletedByCourse(courseService.findByName(name));
        return toReturn;
    }

    public String removeTeacherByName(String name) {
        Teacher teacher = teacherService.findByName(name);
        if (teacher == null) {
            return Message.teacherNotExist(name);
        }
        courseService.setTeacherNull(teacher);
        return teacherService.remove(name);

    }

    public String removeStudentFromCourse(String studentName, String courseName) {
        Student student = studentService.findByName(studentName);
        if (student == null) {
            return Message.studentNotExists(studentName);
        }
        Course course = courseService.findByName(courseName);
        if (course == null) {
            return Message.courseNotExist(courseName);
        }
        if (!course.getStudents().contains(student)) {
            return "Student " + student.getName() + " isn't enrolled in course " + course.getName() + ".";
        }

        course.getStudents().remove(student);
        student.getCourses().remove(course);
        List<Grade> grades = gradeService.findByStudentAndCourse(student, course);
        grades.forEach(g -> {
            g.changeDeleted();
            gradeService.save(g);
        });
        courseService.save(course);
        studentService.save(student);
        return "Student " + studentName + " successfully removed from " + courseName + " course.";
    }

    public String updateStudentAge(String name, int newAge) {
        return studentService.updateAge(name, newAge);
    }

    public String updateTeacherDegree(String name, String newDegree) {
        return teacherService.updateTeacherDegree(name, newDegree);
    }

    public String updateCourseDuration(String name, int newDuration) {
        return courseService.updateDuration(name, newDuration);
    }

    public String purgeAll() {
        purgeGrades();
        purgeCourses();
        purgeTeachers();
        purgeStudents();
        return "Deleted entries removed for real.";
    }

    private boolean purgeCourses() {
        Set<Course> courses = courseService.getByDeletedTrue();
        for (Course course : courses) {
            gradeService.updateDeletedByCourse(course);
        }
        purgeGrades();
        return courseService.purge();
    }

    private boolean purgeStudents() {
        Set<Student> students = studentService.getForDeletion();
        for (Student student : students) {
            Set<Course> courses = student.getCourses();
            for (Course course : courses) {
                course.getStudents().remove(student);
                courseService.save(course);
            }
            student.getCourses().removeAll(courses);
            studentService.save(student);
            gradeService.deleteByStudents(student);
        }
        return studentService.purge();
    }

    private boolean purgeGrades() {
        return gradeService.purge();
    }

    private boolean purgeTeachers() {
        return teacherService.purge();
    }

    public List<StudentDto> getAllStudents() {
      return  studentService.getAllDto();
    }
}