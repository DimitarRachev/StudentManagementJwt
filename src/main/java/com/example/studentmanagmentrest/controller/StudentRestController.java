package com.example.studentmanagmentrest.controller;

import com.example.studentmanagmentrest.model.dto.StudentDto;
import com.example.studentmanagmentrest.model.dto.StudentWithAgeDto;
import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.model.binding.BindingStudentDto;
import com.example.studentmanagmentrest.utility.ResponseMaker;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("students")
public class StudentRestController {
    private final DBFacade dbFacade;

    public StudentRestController(DBFacade dbFacade) {
        this.dbFacade = dbFacade;
    }

    @GetMapping("/{name}")
    public ResponseEntity<String> getAvgForStudent(@PathVariable String name) {
        String response = dbFacade.getAvgForStudent(name);
        return ResponseMaker.makeCannot(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<String> addStudent(@RequestBody BindingStudentDto student) {
        String response = dbFacade.addNewStudent(student.getName(), student.getAge());
        return ResponseMaker.makeSuccess(response);
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<String> removeStudent(@PathVariable String name) {
        String response = dbFacade.removeStudentByName(name);
        return ResponseMaker.makeSuccess(response);
    }

    @PatchMapping
    public ResponseEntity<String> updateStudentAge(@RequestBody BindingStudentDto student) {
        String response = dbFacade.updateStudentAge(student.getName(), student.getAge());
        return ResponseMaker.makeCannot(response);
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = dbFacade.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/withAge")
    public ResponseEntity<List<StudentWithAgeDto>> getStudentsWithAge() {
        List<StudentWithAgeDto> students = dbFacade.getAllWithAge();
        return ResponseEntity.ok(students);
    }
}
