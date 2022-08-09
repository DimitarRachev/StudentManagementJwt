package com.example.studentmanagmentrest.enumeration;

import javax.persistence.Column;
import javax.persistence.Id;

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
