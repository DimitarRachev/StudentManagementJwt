package com.example.studentmanagmentrest.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.service.UserService;
import com.example.studentmanagmentrest.utility.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
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
    Gson gson ;


    public HomeRestController(DBFacade dbFacade,
                              AuthenticationManager authenticationManager,
                              UserService userService,
                              TokenGenerator tokenGenerator) {
        this.dbFacade = dbFacade;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenGenerator = tokenGenerator;
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
                //TODO make utility method ang hide secret in database
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Date refreshExpiresAt = decodedJWT.getExpiresAt();
                UserDetails user = userService.loadUserByUsername(username);
                Date accessExpiresAt = new Date(System.currentTimeMillis() + 1 * 60 * 1000);
                String accessToken = tokenGenerator.makeAccessToken(user, request, algorithm, accessExpiresAt);
                //check for expiration and provide new refresh token if close to expiration

                Duration RefreshTimeToLive = Duration.between(new Date().toInstant(), refreshExpiresAt.toInstant());
                if (RefreshTimeToLive.getSeconds() < 30 * 60) {
                    refreshExpiresAt = new Date(System.currentTimeMillis() + 120 * 60 * 1000);
                    refreshToken = tokenGenerator.makeRefreshToken(user, request, algorithm, refreshExpiresAt);
                }


                Map<String, String> tockens = new HashMap<>();
                tockens.put("access_token", accessToken);
                tockens.put("access_expires_at", gson.toJson( accessExpiresAt));
                tockens.put("refresh_token", refreshToken);
                tockens.put("refresh_expires_at", gson.toJson(refreshExpiresAt));

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tockens);

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


    //TODO Scrap that end poin, becouse it's not used any more
//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody AuthenticationRequest request) {
//        Gson gson = new Gson();
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//        } catch (BadCredentialsException e) {
//            return  new ResponseEntity<>("Invalid username or password!", HttpStatus.BAD_REQUEST);
//        }
//
//        final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
//
//        return  new ResponseEntity<>(HttpStatus.OK);
//    }
}
