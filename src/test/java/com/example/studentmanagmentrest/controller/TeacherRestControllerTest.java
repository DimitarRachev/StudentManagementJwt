package com.example.studentmanagmentrest.controller;

import com.example.studentmanagmentrest.model.binding.TeacherBindingDto;
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
class TeacherRestControllerTest {

    @MockBean
    DBFacade dbFacade;

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteTeacherSuccess() throws Exception {
        when(dbFacade.removeTeacherByName("Me")).thenReturn("Teacher Me successfully removed from the database.");

        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/Me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Teacher Me successfully removed from the database."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteTeacherFail() throws Exception {
        when(dbFacade.removeTeacherByName("Me")).thenReturn("Teacher Me cannot be found in the database.");

        mockMvc.perform(MockMvcRequestBuilders.delete("/teachers/Me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Teacher Me cannot be found in the database."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addTeacherSuccess() throws Exception {
        Gson gson = new Gson();
        TeacherBindingDto teacher = new TeacherBindingDto();
        teacher.setName("Me");
        teacher.setDegree("some degree");
        when(dbFacade.addNewTeacher(teacher.getName(), teacher.getDegree())).thenReturn( "Teacher Me successfully added to database");
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(teacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Teacher Me successfully added to database"));

    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addTeacherFail() throws Exception {
            Gson gson = new Gson();
        TeacherBindingDto teacher = new TeacherBindingDto();
        teacher.setName("Me");
        teacher.setDegree("some degree");
        when(dbFacade.addNewTeacher(teacher.getName(), teacher.getDegree())).thenReturn( "Teacher Me is already in the database");
        mockMvc.perform(MockMvcRequestBuilders.post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(teacher)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Teacher Me is already in the database"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateTeacher() throws Exception {
        Gson gson = new Gson();
        TeacherBindingDto teacher = new TeacherBindingDto();
        teacher.setName("Me");
        teacher.setDegree("some degree");
        when( dbFacade.updateTeacherDegree(teacher.getName(), teacher.getDegree())).thenReturn("Me's degree updated to some degree.");

        mockMvc.perform(MockMvcRequestBuilders.patch("/teachers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(teacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Me's degree updated to some degree."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateTeacherFail() throws Exception {
        Gson gson = new Gson();
        TeacherBindingDto teacher = new TeacherBindingDto();
        teacher.setName("Me");
        teacher.setDegree("some degree");
        when( dbFacade.updateTeacherDegree(teacher.getName(), teacher.getDegree())).thenReturn("Teacher Me cannot be found in the database");

        mockMvc.perform(MockMvcRequestBuilders.patch("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(teacher)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Teacher Me cannot be found in the database"));
    }
}