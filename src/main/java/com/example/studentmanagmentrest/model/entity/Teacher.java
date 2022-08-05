package com.example.studentmanagmentrest.model.entity;


import com.example.studentmanagmentrest.enumeration.UserRole;
import com.google.common.collect.Sets;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Teacher extends UserEntity{

    @Column
    private String degree;

    @Column
    private boolean deleted;

    public Teacher(String name, String degree) {
        this();
        super.name = name;
        this.degree = degree;
        deleted = false;

    }   public Teacher(String name, String degree, String username, String password) {
        this();
        super.name = name;
        this.degree = degree;
        deleted = false;
        setUsername(username);
        setPassword(password);

    }

    public Teacher() {
        setRoles(Sets.newHashSet(UserRole.TEACHER));
    }

    public String getDegree() {
        return degree;
    }

    public Teacher setDegree(String degree) {
        this.degree = degree;
        return this;
    }

    public boolean deleted() {
        return deleted;
    }

    public Teacher changeDeleted() {
        this.deleted = !this.deleted;
        return this;
    }
}
