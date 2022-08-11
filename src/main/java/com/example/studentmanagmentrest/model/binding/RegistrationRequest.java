package com.example.studentmanagmentrest.model.binding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationRequest {
    private final String username;
    private final String email;
    private final String password;
    private final int age;
}
