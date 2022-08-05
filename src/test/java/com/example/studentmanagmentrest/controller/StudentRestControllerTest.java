package com.example.studentmanagmentrest.controller;

import com.example.studentmanagmentrest.model.binding.BindingStudentDto;
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
class StudentRestControllerTest {

    @MockBean
    private DBFacade dbFacade;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "STUDENT")
    void getAvgForStudentSuccess() throws Exception {
        when(dbFacade.getAvgForStudent("Me")).thenReturn("Me's average grade is: 6.00");
        mockMvc.perform(MockMvcRequestBuilders.get("/students/Me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Me's average grade is: 6.00"));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void getAvgForStudentFail() throws Exception {
        when(dbFacade.getAvgForStudent("Me")).thenReturn("Student Me cannot be found in the database.");
        mockMvc.perform(MockMvcRequestBuilders.get("/students/Me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Student Me cannot be found in the database."));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addStudentSuccess() throws Exception {
        Gson gson = new Gson();
        BindingStudentDto studentDto = new BindingStudentDto();
        studentDto.setName("Me");
        studentDto.setAge(15);
        when(dbFacade.addNewStudent("Me", 15)).thenReturn("Student Me successfully added in database");
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(studentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Student Me successfully added in database"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addStudentFail() throws Exception {
        Gson gson = new Gson();
        BindingStudentDto studentDto = new BindingStudentDto();
        studentDto.setName("Me");
        studentDto.setAge(15);
        when(dbFacade.addNewStudent("Me", 15)).thenReturn("Student Me is already in database");
        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Student Me is already in database"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void removeStudentSuccess() throws Exception {
        when(dbFacade.removeStudentByName("Me")).thenReturn("Student Me successfully deleted from database");
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/Me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Student Me successfully deleted from database"));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void removeStudentFail() throws Exception {
        when(dbFacade.removeStudentByName("Me")).thenReturn("Student Me cannot be found in the database.");
        mockMvc.perform(MockMvcRequestBuilders.delete("/students/Me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Student Me cannot be found in the database."));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void updateStudentAgeSuccess() throws Exception {
        Gson gson = new Gson();
        BindingStudentDto studentDto = new BindingStudentDto();
        studentDto.setName("Me");
        studentDto.setAge(1);
        when(dbFacade.updateStudentAge(studentDto.getName(), studentDto.getAge())).thenReturn("Me's age updated to 1.");

        mockMvc.perform(MockMvcRequestBuilders.patch("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(studentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Me's age updated to 1."));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void updateStudentAgeFail() throws Exception {
        Gson gson = new Gson();
        BindingStudentDto studentDto = new BindingStudentDto();
        studentDto.setName("Me");
        studentDto.setAge(1);
        when(dbFacade.updateStudentAge(studentDto.getName(), studentDto.getAge())).thenReturn("Student Me cannot be found in the database.");

        mockMvc.perform(MockMvcRequestBuilders.patch("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(studentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Student Me cannot be found in the database."));
    }
}