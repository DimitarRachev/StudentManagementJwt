package com.example.studentmanagmentrest.repository;


import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher getTeacherByNameAndDeletedFalse(String name);

    Teacher findByNameAndDeletedFalse(String teacherName);

    Teacher findByNameAndDegreeAndDeletedFalse(String name, String degree);

    boolean existsByNameAndDeletedFalse(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Teacher SET degree = :degree WHERE name = :name")
    int updateDegree(String degree, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Teacher SET deleted = true WHERE name = :name")
    int deleteByName(String name);

    @Modifying
    @Transactional
    void deleteAllByDeletedTrue();

    Optional<Teacher> findByUsernameOrEmailAndDeletedFalse(String username, String email);

    Page<Teacher> findAllByDeletedFalse(Pageable pageable);
}
