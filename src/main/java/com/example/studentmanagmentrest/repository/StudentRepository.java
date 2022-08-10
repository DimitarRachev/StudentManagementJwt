package com.example.studentmanagmentrest.repository;


import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> getByDeletedFalse();

    List<Student> getAllByCoursesContainingAndDeletedFalse(Course course);


    Student getStudentByNameAndDeletedFalse(String name);

    Student findByNameAndDeletedFalse(String name);

    @Transactional
    @Modifying
    @Query("UPDATE Student SET deleted = true WHERE name = :name")
    void deleteByName(String name);

    boolean existsByNameAndDeletedFalse(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Student SET age = :newAge WHERE name = :name")
    void updateAge(String name, int newAge);

    @Modifying
    @Transactional
    void deleteAllByDeletedTrue();

    Set<Student> getByDeletedTrue();

    Optional<UserEntity> findByUsernameAndDeletedFalse(String username);
}
