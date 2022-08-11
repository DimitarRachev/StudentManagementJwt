package com.example.studentmanagmentrest.controller;


import com.example.studentmanagmentrest.model.binding.RegistrationRequest;
import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.service.RegistrationService;
import com.example.studentmanagmentrest.service.UserService;
import com.example.studentmanagmentrest.utility.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
public class HomeRestController {
    private final DBFacade dbFacade;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenGenerator tokenGenerator;
    private final RegistrationService registrationService;
    Gson gson;


    public HomeRestController(DBFacade dbFacade,
                              AuthenticationManager authenticationManager,
                              UserService userService,
                              TokenGenerator tokenGenerator, RegistrationService registrationService) {
        this.dbFacade = dbFacade;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
        this.registrationService = registrationService;
        this.gson = new Gson();
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generate() {
        try {
            dbFacade.makeDatabase();
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/purge")
    public ResponseEntity<String> purgeDB() {
        String response = dbFacade.purgeAll();
        return new ResponseEntity<>(gson.toJson(response), HttpStatus.OK);
    }

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring(7);
                Map<String, String> tokens = tokenGenerator.generateRefreshTokens(request, refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        try {
            String response = registrationService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token, @RequestParam("user") String username) {

        try {
            String response = registrationService.confirm(token, username);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/register/refresh")
    public ResponseEntity<String> refresh(@RequestParam("user") String user) {
        try {
            String response = registrationService.getNewToken(user);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
