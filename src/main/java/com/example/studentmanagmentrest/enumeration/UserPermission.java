package com.example.studentmanagmentrest.enumeration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * should be deprecated
 */

public enum UserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write"),
    GRADE_READ("grade:read"),
    GRADE_WRITE("grade:write"),
    TEACHER_READ("teacher:read"),
    TEACHER_WRITE("teacher:write"),
    DATABASE_WRITE("database:write");;


    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
