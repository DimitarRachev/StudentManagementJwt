package com.example.studentmanagmentrest.controller;

import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class HomeRestControllerTest {

    @MockBean
    private DBFacade dbFacade;


    @Autowired
    private MockMvc mockMvc;




    @Test
    void generate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/generate"))
                .andExpect(status().isCreated());
    }

    @Test
    void generateFail() throws Exception {
        when(dbFacade.makeDatabase()).thenThrow(IOException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/generate"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    void purgeDB() throws Exception {
        when(dbFacade.purgeAll()).thenReturn("Deleted entries removed for real.");
        mockMvc.perform(MockMvcRequestBuilders.delete("/purge")
                        .param("username", "test")
                        .param("password", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deleted entries removed for real."));
    }
}