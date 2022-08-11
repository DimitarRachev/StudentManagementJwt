package com.example.studentmanagmentrest.model.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class User implements UserDetails {
    private  final UserEntity user;

    public User(UserEntity user) { this.user = user; }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles().stream()
            .map(p -> new SimpleGrantedAuthority("ROLE_"+p.name()))
            .collect(Collectors.toSet());
    }

    public  boolean isTeacher() {return user instanceof Teacher;}
    public  boolean isStudent() {return user instanceof Student;}


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled;
    }


}
