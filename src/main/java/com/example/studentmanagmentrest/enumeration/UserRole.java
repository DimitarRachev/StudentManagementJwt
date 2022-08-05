package com.example.studentmanagmentrest.enumeration;

import com.google.common.collect.Sets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

import static com.example.studentmanagmentrest.enumeration.UserPermission.COURSE_READ;
import static com.example.studentmanagmentrest.enumeration.UserPermission.COURSE_WRITE;
import static com.example.studentmanagmentrest.enumeration.UserPermission.DATABASE_WRITE;
import static com.example.studentmanagmentrest.enumeration.UserPermission.GRADE_READ;
import static com.example.studentmanagmentrest.enumeration.UserPermission.GRADE_WRITE;
import static com.example.studentmanagmentrest.enumeration.UserPermission.STUDENT_READ;
import static com.example.studentmanagmentrest.enumeration.UserPermission.STUDENT_WRITE;
import static com.example.studentmanagmentrest.enumeration.UserPermission.TEACHER_READ;
import static com.example.studentmanagmentrest.enumeration.UserPermission.TEACHER_WRITE;

public enum UserRole {
//    STUDENT(Sets.newHashSet(STUDENT_READ, GRADE_READ)),
//    TEACHER(Sets.newHashSet(STUDENT_READ, GRADE_READ, GRADE_WRITE, COURSE_READ)),
//    MANAGER(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, GRADE_READ, GRADE_WRITE, COURSE_READ, COURSE_WRITE, TEACHER_READ, TEACHER_WRITE)),
//    ADMIN((Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, GRADE_READ, GRADE_WRITE,
//            COURSE_READ, COURSE_WRITE, TEACHER_READ, TEACHER_WRITE, DATABASE_WRITE)));

   STUDENT,
   TEACHER,
   MANAGER,
   ADMIN;
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

//    private final Set<UserPermission> permissions;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    UserRole(Set<UserPermission> permissions) {
//        this.permissions = permissions;
//    }
//
//    public Set<UserPermission> getPermissions() {
//        return permissions;
//    }
}
