package com.example.studentmanagmentrest.service;



import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.model.dto.CourseDto;

import java.util.List;
import java.util.Set;

public interface CourseService {
    void save(Course course);

    String addTeacher(String teacherName, String courseName);

    String addStudent(String studentName, String courseName);


    List<Course> getAll();

    Course findByNameAndTotalHours(String name, int hours);

    Course findByName(String name);

    List<Course> findAll();

    Course getByName(String name);

    List<Course> getByTeacher(Teacher teacher);

    String add(String name, int hours);

    boolean courseExists(String name);

    List<Course> getCoursesDto();

    String updateDuration(String name, int newDuration);

    String removeCourseByName(String name);

    List<CourseDto> makeCoursesDto();

    boolean purge();

    void setTeacherNull(Teacher teacher);

    Set<Course> getByDeletedTrue();
}
