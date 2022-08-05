package com.example.studentmanagmentrest.repository;


import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentAndCourse(Student student, Course course);
    List<Grade> getAllByStudentAndDeletedFalse(Student student);

    @Modifying
    @Transactional
    void deleteGradesByStudent(Student student);

    List<Grade> findByStudent(Student student);

    List<Grade> getAllByCourse(Course course);

    @Transactional
    @Modifying
    @Query("UPDATE Grade SET deleted = true WHERE course = :course")
    int updateDeletedByCourse(Course course);

    @Modifying
    @Transactional
    void deleteAllByDeletedTrue();

    @Modifying
    @Transactional
    void deleteByStudent(Student student);
}
