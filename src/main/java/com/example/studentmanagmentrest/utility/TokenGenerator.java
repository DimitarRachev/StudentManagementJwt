package com.example.studentmanagmentrest.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

public class TokenGenerator {

    public String makeAccessToken(UserDetails user,
                                  HttpServletRequest request,
                                  Algorithm algorithm,
                                  Date expiresAt) {

        return  JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAt)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public  String makeRefreshToken(UserDetails user,
                                    HttpServletRequest request,
                                    Algorithm algorithm,
                                    Date expiresAt) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAt)
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }
}
