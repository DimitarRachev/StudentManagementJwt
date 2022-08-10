package com.example.studentmanagmentrest.utility.dbInitializer;


import com.example.studentmanagmentrest.enumeration.UserRole;
import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.service.CourseService;
import com.example.studentmanagmentrest.service.GradeService;
import com.example.studentmanagmentrest.service.StudentService;
import com.example.studentmanagmentrest.service.TeacherService;
import com.google.common.collect.Sets;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Initializer {
    private final PasswordEncoder passwordEncoder;
    private TeacherService teacherService;
    private StudentService studentService;
    private CourseService courseService;
    private GradeService gradeService;

    private Random random;

    public Initializer(TeacherService teacherService, StudentService studentService, CourseService courseService, GradeService gradeService, PasswordEncoder passwordEncoder) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.gradeService = gradeService;
        this.teacherService = teacherService;
        this.passwordEncoder = passwordEncoder;
        this.random = new Random();
    }

    public void run() throws IOException {

        addTeachers();
        addStudents();
        addCourses();
        addTeachersToCourses();
        addStudentsToCoursesWithGrades();
    }


    private void addTeachersToCourses() {
        List<Teacher> teachers = teacherService.getAll();
        List<Course> courses = courseService.getAll();
        for (Course course : courses) {
            course.setTeacher(teachers.get(random.nextInt(teachers.size())));
            courseService.save(course);
        }
    }

    private void addStudentsToCoursesWithGrades() {
        List<Student> students = studentService.getAll();
        List<Course> courses = courseService.getAll();
        for (Course course : courses) {
            List<Student> participants = getParticipants(students);
            for (Student student : participants) {
                List<Double> grades = getGrades();
                for (Double grade : grades) {
                    Grade score = new Grade(student, course, grade);
                    gradeService.save(score);
                    student.getGrades().add(score);
                }
                course.getStudents().add(student);
            }
            courseService.save(course);
        }
    }

    private List<Double> getGrades() {
        List<Double> toReturn = new ArrayList<>();
        int size = random.nextInt(5);
        for (int i = 0; i < size; i++) {
            toReturn.add((random.nextInt(400) + 200) / 100.00);
        }
        return toReturn;
    }

    private List<Student> getParticipants(List<Student> students) {
        List<Student> toReturn = new ArrayList<>();
        int size = random.nextInt(students.size() - 4);
        for (int i = 0; i < size; i++) {
            toReturn.add(students.get(random.nextInt(students.size())));
        }
        return toReturn;
    }

    private void addCourses() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src\\main\\resources\\courses.txt"));
        lines.stream().map(s -> s.split(" "))
                .map(m -> new Course(m[0], Integer.parseInt(m[1])))
                .forEach(se -> courseService.save(se));
    }

    private void addStudents() throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src\\main\\resources\\students.txt"));
        lines.stream().map(s -> s.split(" "))
                .map(m -> new Student(m[0], Integer.parseInt(m[1]), m[2], passwordEncoder.encode(m[3])))
                .forEach(se -> studentService.save(se));
    }

    private void addTeachers() throws IOException {
        Teacher admin = new Teacher("admin", "admin", "admin", passwordEncoder.encode("admin"));
        admin.setRoles(Sets.newHashSet(UserRole.ADMIN));
        teacherService.save(admin);
        List<String> lines = Files.readAllLines(Path.of("src\\main\\resources\\teachers.txt"));
        lines.stream().map(s -> s.split(" "))
                .forEach(m -> teacherService.save(new Teacher(m[0], m[1], m[2], passwordEncoder.encode(m[3]))));

    }
}
