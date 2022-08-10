package com.example.studentmanagmentrest.service.impl;


import com.example.studentmanagmentrest.model.dto.CourseDto;
import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.repository.CourseRepository;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.CourseService;
import com.example.studentmanagmentrest.utility.Message;
import com.example.studentmanagmentrest.utility.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public void save(Course c1) {
        courseRepository.save(c1);
    }

    @Override
    public String addTeacher(String teacherName, String courseName) {
        Course course = courseRepository.findByNameAndDeletedFalse(courseName);
        if (course == null) {
            return Message.courseNotExist(courseName);
        }
        Teacher teacher = teacherRepository.findByNameAndDeletedFalse(teacherName);
        if (teacher == null) {
            return Message.teacherNotExist(teacherName);
        }
        course.setTeacher(teacher);
        courseRepository.save(course);
        return "Teacher " + teacherName + " now in charge for course " + courseName;
    }

    @Override
    public String addStudent(String studentName, String courseName) {
        Student student = studentRepository.findByNameAndDeletedFalse(studentName);
        if (student == null) {
            return Message.studentNotExists(studentName);
        }
        Course course = courseRepository.findByNameAndDeletedFalse(courseName);
        if (course == null) {
            return Message.courseNotExist(courseName);
        }

        Set<Student> students = course.getStudents();
        if (students.contains(student)) {
            return "Student " + studentName + " is already enrolled in course " + courseName + ".";
        }
        course.getStudents().add(student);
        student.getCourses().add(course);
        courseRepository.save(course);
        studentRepository.save(student);
        return " Student " + studentName + " successfully enrolled in course " + courseName + ".";
    }


    @Override
    public List<Course> getAll() {
        return courseRepository.getByDeletedIsFalseOrderByName();
    }

    @Override
    public Course findByNameAndTotalHours(String name, int hours) {
        return courseRepository.findByNameAndTotalHoursAndDeletedFalse(name, hours);
    }

    @Override
    public Course findByName(String name) {
        return courseRepository.findByNameAndDeletedFalse(name);
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.getByDeletedIsFalseOrderByName();
    }

    @Override
    public Course getByName(String name) {
        return courseRepository.findByNameAndDeletedFalse(name);
    }

    @Override
    public List<Course> getByTeacher(Teacher teacher) {
        return courseRepository.getByTeacherAndDeletedFalse(teacher);
    }

    @Override
    public String add(String name, int hours) {
        if (courseExists(name)) {
            return "Course " + name + " already in database.";
        } else {
            courseRepository.save(new Course(name, hours));
            return "Course " + name + " saved successfully.";
        }
    }

    @Override
    public  boolean courseExists(String name) {
        return courseRepository.existsByNameAndDeletedFalse(name);
    }

    @Override
    public List<Course> getCoursesDto() {
        return courseRepository.getAllByDeletedFalse();
    }

    @Override
    public String updateDuration(String name, int newDuration) {
        if (courseExists(name)) {
            courseRepository.updateDuration(newDuration, name);
            return name + "'s duration changed to " + newDuration + " hours.";
        } else  {
            return Message.courseNotExist(name);
        }
    }

    @Override
    public String removeCourseByName(String name) {
        Course course = getByName(name);
        if (course == null) {
            return Message.courseNotExist(name);
        }
        courseRepository.updateDeleted(name);
        return "Course " + name + " successfully removed from the database.";
    }

    @Override
    public List<CourseDto> makeCoursesDto() {
        return courseRepository.getAllByDeletedFalse().stream().map(DtoConverter::makeCoursesDto).collect(Collectors.toList());
    }

    @Override
    public boolean purge() {
        courseRepository.deleteAllByDeletedTrue();
        return true;
    }

    @Override
    public void setTeacherNull(Teacher teacher) {
        courseRepository.setTeacherNull(teacher);
    }

    @Override
    public Set<Course> getByDeletedTrue() {
        return courseRepository.getAllByDeletedTrue();
    }
}
