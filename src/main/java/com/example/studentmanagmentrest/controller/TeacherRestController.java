package com.example.studentmanagmentrest.controller;


import com.example.studentmanagmentrest.model.binding.TeacherBindingDto;
import com.example.studentmanagmentrest.model.dto.TeacherDto;
import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.utility.ResponseMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping("teachers")
public class TeacherRestController {

    private final DBFacade dbFacade;

    @Autowired
    public TeacherRestController(DBFacade dbFacade) {
        this.dbFacade = dbFacade;
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<String> deleteTeacher(@PathVariable String name) {
        String response = dbFacade.removeTeacherByName(name);
        return ResponseMaker.makeSuccess(response);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public ResponseEntity<String> addTeacher(@RequestBody TeacherBindingDto teacher) {
        String response = dbFacade.addNewTeacher(teacher.getName(), teacher.getDegree());
        return ResponseMaker.makeSuccess(response);
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<String> updateTeacher(@RequestBody TeacherBindingDto teacher) {
        String response = dbFacade.updateTeacherDegree(teacher.getName(), teacher.getDegree());
        return ResponseMaker.makeCannot(response);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'MANAGER', 'ADMIN')")
    public ResponseEntity<Page<TeacherDto>> getAll(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "ASC") Sort.Direction order,
                                                   @RequestParam(defaultValue = "name") String sortField) {
        Page<TeacherDto> teachers = dbFacade.getAllTeachers(page, size, order, sortField);
        return ResponseEntity.ok(teachers);
    }
}
