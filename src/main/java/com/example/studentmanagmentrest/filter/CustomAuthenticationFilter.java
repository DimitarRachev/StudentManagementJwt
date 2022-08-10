package com.example.studentmanagmentrest.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.studentmanagmentrest.model.AuthenticationRequest;
import com.example.studentmanagmentrest.utility.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final Gson gson;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, TokenGenerator tokenGenerator) {
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
        gson = new Gson();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
//            Used if username/password are send as headers
//            String username = request.getParameter("username");
//            String password = request.getParameter("password");
            AuthenticationRequest json = gson.fromJson(request.getReader(), AuthenticationRequest.class);
            String username = json.getUsername();
            String password = json.getPassword();
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Object principal = authResult.getPrincipal();
        UserDetails user;
        if (principal instanceof UserDetails) {
            user = (UserDetails) principal;
        } else {
            throw new RuntimeException("Problem while getting principal from authResult: " + principal);
        }

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        Date accessExpiresAt = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        String accessToken =  tokenGenerator.makeAccessToken(user, request, algorithm, accessExpiresAt);


        Date refreshExpiresAt = new Date(System.currentTimeMillis() + 120 * 60 * 1000);
        String refreshToken = tokenGenerator.makeRefreshToken(user, request, algorithm, refreshExpiresAt);

        Map<String, String> tockens = new HashMap<>();
        tockens.put("access_token", accessToken);
        tockens.put("access_expires_at", gson.toJson( accessExpiresAt));
        tockens.put("refresh_token", refreshToken);
        tockens.put("refresh_expires_at", gson.toJson(refreshExpiresAt));

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tockens);

    }
}
