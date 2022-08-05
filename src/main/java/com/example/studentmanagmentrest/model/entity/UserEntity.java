package com.example.studentmanagmentrest.model.entity;


import com.example.studentmanagmentrest.enumeration.UserRole;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.Set;

@MappedSuperclass
public class UserEntity extends NameEntity {

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @ElementCollection(targetClass = UserRole.class)
    @CollectionTable()
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;

    @Column
    boolean isAccountNonExpired = true;

    @Column
    boolean isAccountNonLocked = true;

    @Column
    boolean isCredentialsNonExpired = true;

    @Column(columnDefinition = "boolean default true")
    boolean isEnabled = true;


    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public UserEntity setRoles(Set<UserRole> roles) {
        this.roles = roles;
        return this;
    }

    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public UserEntity setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
        return this;
    }

    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public UserEntity setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
        return this;
    }

    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public UserEntity setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
        return this;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public UserEntity setEnabled(boolean enabled) {
        isEnabled = enabled;
        return this;
    }
}
