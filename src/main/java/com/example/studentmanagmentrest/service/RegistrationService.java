package com.example.studentmanagmentrest.service;

import com.example.studentmanagmentrest.model.binding.RegistrationRequest;

public interface RegistrationService {
    String register(RegistrationRequest request);

    String confirm(String token, String username);
    String getNewToken(String name);
}
