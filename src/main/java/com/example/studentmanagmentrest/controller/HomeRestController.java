package com.example.studentmanagmentrest.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.studentmanagmentrest.service.DBFacade;
import com.example.studentmanagmentrest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
public class HomeRestController {
    private final DBFacade dbFacade;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public HomeRestController(DBFacade dbFacade, AuthenticationManager authenticationManager, UserService userService) {
        this.dbFacade = dbFacade;
        this.authenticationManager = authenticationManager;
        this.userService = userService;

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
        return new ResponseEntity<>(response, HttpStatus.OK);
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

                UserDetails user = userService.loadUserByUsername(username);

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                        .sign(algorithm);
                //TODO maybe check for expiration and provide new refresh token if close to expiration?
//                Date expiresAt = decodedJWT.getExpiresAt();
//                Duration TTL = Duration.between(new Date().toInstant(), expiresAt.toInstant());
//                if (TTL.getSeconds() < 15 * 60) {
//                    refreshToken = JWT.create()
//                            .withSubject(user.getUsername())
//                            .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
//                            .withIssuer(request.getRequestURL().toString())
//                            .sign(algorithm);
//                }


                Map<String, String> tockens = new HashMap<>();
                tockens.put("access_token", accessToken);
                tockens.put("refresh_token", refreshToken);

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
            throw new RuntimeException("Refresh token is missing or expired!");
        }
    }
}
