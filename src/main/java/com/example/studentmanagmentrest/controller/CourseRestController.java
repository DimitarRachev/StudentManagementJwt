package com.example.studentmanagmentrest.controller;


import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.model.dto.CourseDto;
import com.example.studentmanagmentrest.model.dto.CourseDtoWithGrades;
import com.example.studentmanagmentrest.model.dto.StudentDtoAvgGrade;
import com.example.studentmanagmentrest.model.binding.CourseBindingDto;
import com.example.studentmanagmentrest.utility.ResponseMaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/courses")

public class CourseRestController {
    private final DBFacade dbFacade;

    public CourseRestController(DBFacade dbFacade) {
        this.dbFacade = dbFacade;
    }

    @GetMapping("/allWithoutGrades")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    ResponseEntity<List<CourseDto>> getCoursesDto() {
        List<CourseDto> coursesDto = dbFacade.getCoursesDto();
        if (coursesDto == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(coursesDto, HttpStatus.OK);
    }

    @GetMapping("/allWithGrades")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    ResponseEntity<List<CourseDtoWithGrades>> getCoursesDtoWithGrades() {
        List<CourseDtoWithGrades> coursesDtoWithGrades = dbFacade.getCoursesDtoWithGrades();
        return new ResponseEntity<>(coursesDtoWithGrades, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    ResponseEntity<String> addCourse(@RequestBody CourseBindingDto course) {
        System.out.println(course.getName() +" "+ course.getDuration());
        String response = dbFacade.addNewCourse(course.getName(), course.getDuration());
        return ResponseMaker.makeSaved(response);
    }

    @PutMapping("/{courseName}/addTeacher/{teacherName}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    ResponseEntity<String> addTeacher(@PathVariable String teacherName, @PathVariable String courseName) {
        System.out.println();
        String response = dbFacade.addTeacherToCourse(teacherName, courseName);
        return ResponseMaker.makeCannot(response);
    }



    @PutMapping("/{courseName}/addStudent/{studentName}")
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER', 'ADMIN')")
    ResponseEntity<String> addStudent(@PathVariable String studentName, @PathVariable String courseName) {
        String response = dbFacade.addStudentToCourse(studentName, courseName);
        return ResponseMaker.makeSuccess(response);
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<String> deleteCourse(@PathVariable String name) {
        String response = dbFacade.removeCourseByName(name);
        return ResponseMaker.makeSuccess(response);
    }

    @PutMapping("/{courseName}/removeStudent/{studentName}")
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<String> removeStudentFromCourse(@PathVariable String studentName, @PathVariable String courseName) {
        String response = dbFacade.removeStudentFromCourse(studentName, courseName);
        return ResponseMaker.makeSuccess(response);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole( 'MANAGER', 'ADMIN')")
    public ResponseEntity<String> updateCourseDuration(@RequestBody CourseBindingDto course) {
        String response = dbFacade.updateCourseDuration(course.getName(), course.getDuration());
        return ResponseMaker.makeCannot(response);
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<List<StudentDtoAvgGrade>> getAvgForCourse(@PathVariable String name) {
        List<StudentDtoAvgGrade> average = dbFacade.getAvgForStudentsInCourse(name);
        if (average == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(average, HttpStatus.OK);
    }


}
