package com.example.studentmanagmentrest.repository;

import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> getByDeletedIsFalseOrderByName();

    Course findByNameAndTotalHoursAndDeletedFalse(String name, int hours);

    Course findByNameAndDeletedFalse(String name);

    List<Course> getByTeacherAndDeletedFalse(Teacher teacher);

    boolean existsByNameAndDeletedFalse(String name);


    List<Course> getAllByDeletedFalse();

    @Modifying
    @Transactional
    @Query("UPDATE Course SET totalHours = :newDuration WHERE name = :name")
    void updateDuration(int newDuration, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Course SET deleted = true WHERE name = :name")
    void updateDeleted(String name);

    @Modifying
    @Transactional
    void deleteAllByDeletedTrue();

    @Modifying
    @Transactional
    @Query("UPDATE Course SET teacher = NULL WHERE teacher = :teacher")
    void setTeacherNull(Teacher teacher);

    Set<Course> getAllByDeletedTrue();
}
