package com.example.studentmanagmentrest.controller;


import com.example.studentmanagmentrest.model.binding.GradeBindingDto;
import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.utility.ResponseMaker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@RequestMapping("/grades")
public class GradeRestController {
    private final DBFacade dbFacade;

    public GradeRestController(DBFacade dbFacade) {
        this.dbFacade = dbFacade;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> addGrade(@RequestBody GradeBindingDto grade) {
        String response = dbFacade.addGrade(grade.getStudentName(), grade.getCourseName(), grade.getGrade());
        return ResponseMaker.makeCannot(response);
    }


}
