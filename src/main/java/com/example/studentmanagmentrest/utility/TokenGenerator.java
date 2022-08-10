package com.example.studentmanagmentrest.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.studentmanagmentrest.key.repository.KeyRepository;
import com.example.studentmanagmentrest.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@Slf4j
public class TokenGenerator {


    private final UserService userService;


    private final KeyRepository keyRepository;

  private   Algorithm algorithm;
    Gson gson;
//    private final int ACCESS_DURATION = 10 * 60 * 1000;
    // 30sec duration for testing purposes only
    private final int ACCESS_DURATION =   30 * 1000;
    private final int REFRESH_DURATION = 120 * 60 * 1000;

    public TokenGenerator(UserService userService, KeyRepository keyRepository) {
        this.userService = userService;
        this.keyRepository = keyRepository;

        gson = new Gson();
        algorithm = Algorithm.HMAC256(keyRepository.findAll().get(0).getKey().getBytes());
    }



    public String makeAccessToken(UserDetails user,
                                  HttpServletRequest request,
                                  Date expiresAt) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAt)
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String makeRefreshToken(UserDetails user,
                                   HttpServletRequest request,
                                   Date expiresAt) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(expiresAt)
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public Map<String, String> generateTokens(HttpServletRequest request, UserDetails user) {
        Date accessExpiresAt = new Date(System.currentTimeMillis() + ACCESS_DURATION);
        String accessToken = makeAccessToken(user, request, accessExpiresAt);
        Date refreshExpiresAt = new Date(System.currentTimeMillis() + REFRESH_DURATION);
        String refreshToken = makeRefreshToken(user, request, refreshExpiresAt);
        return makeTokensMap(accessToken, refreshToken,
                accessExpiresAt, refreshExpiresAt, user);
    }

    public Map<String, String> generateRefreshTokens(HttpServletRequest request,
                                                     String refreshToken) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        Date refreshExpiresAt = decodedJWT.getExpiresAt();
        UserDetails user = userService.loadUserByUsername(username);
        Date accessExpiresAt = new Date(System.currentTimeMillis() + ACCESS_DURATION);
        String accessToken = makeAccessToken(user, request, accessExpiresAt);
        Duration RefreshTimeToLive = Duration.between(new Date().toInstant(), refreshExpiresAt.toInstant());
        if (RefreshTimeToLive.getSeconds() < 30 * 60) {
            refreshExpiresAt = new Date(System.currentTimeMillis() + REFRESH_DURATION);
            refreshToken = makeRefreshToken(user, request, refreshExpiresAt);
        }
        return  makeTokensMap(accessToken, refreshToken, accessExpiresAt, refreshExpiresAt, user);
    }

    public Map<String, String> makeTokensMap(String accessToken, String refreshToken,
                                             Date accessExpiresAt, Date refreshExpiresAt,
                                             UserDetails user) {
        Map<String, String> tockens = new HashMap<>();
        tockens.put("access_token", accessToken);
        tockens.put("access_expires_at", gson.toJson(accessExpiresAt));
        tockens.put("refresh_token", refreshToken);
        tockens.put("refresh_expires_at", gson.toJson(refreshExpiresAt));
        tockens.put("authorities", gson.toJson(user.getAuthorities()));
        return tockens;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
