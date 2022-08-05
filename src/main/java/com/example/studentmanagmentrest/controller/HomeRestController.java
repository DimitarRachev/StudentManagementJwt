package com.example.studentmanagmentrest.controller;


import com.example.studentmanagmentrest.model.AuthenticationRequest;
import com.example.studentmanagmentrest.model.AuthenticationResponse;
import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.service.JwtUtil;
import com.example.studentmanagmentrest.service.UserService;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
public class HomeRestController {
    private final DBFacade dbFacade;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public HomeRestController(DBFacade dbFacade, AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.dbFacade = dbFacade;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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
    public ResponseEntity<String> purgeDB(){
        String response = dbFacade.purgeAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AuthenticationRequest request) {
        Gson gson = new Gson();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            return  new ResponseEntity<>("Invalid username or password!", HttpStatus.BAD_REQUEST);
        }

        final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        final Date expiresAt = jwtUtil.extractExpiration(jwt);
        return  new ResponseEntity<>(new AuthenticationResponse(jwt, expiresAt),  HttpStatus.OK);
    }
}
