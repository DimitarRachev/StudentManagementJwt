package com.example.studentmanagmentrest.model;

import java.util.Date;

public class AuthenticationResponse {
private final String jwt;
private final Date expiresAt;

    public AuthenticationResponse(String jwt, Date expiresAt) {
        this.jwt = jwt;
        this.expiresAt = expiresAt;
    }

    public String getJwt() {
        return jwt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }
}
