package com.example.studentmanagmentrest.controller;

import com.example.studentmanagmentrest.model.binding.GradeBindingDto;
import com.example.studentmanagmentrest.service.DBFacade;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GradeRestControllerTest {

    @MockBean
    private DBFacade dbFacade;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "TEACHER")
    void addGradeSuccess() throws Exception {
        Gson gson = new Gson();
        GradeBindingDto dto = new GradeBindingDto();
        dto.setStudentName("Me").setCourseName("Java").setGrade(6.00);
        when(dbFacade.addGrade("Me", "Java", 6.00)).thenReturn("Student Me get 6.00 on Java");

        mockMvc.perform(MockMvcRequestBuilders.post("/grades/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Student Me get 6.00 on Java"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addGradeFail() throws Exception {
        Gson gson = new Gson();
        GradeBindingDto dto = new GradeBindingDto();
        dto.setStudentName("Me").setCourseName("Java").setGrade(6.00);
        when(dbFacade.addGrade("Me", "Java", 6.00)).thenReturn("Student Me cannot be found in the database.");

        mockMvc.perform(MockMvcRequestBuilders.post("/grades/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Student Me cannot be found in the database."));
    }
}