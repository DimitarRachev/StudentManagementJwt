package com.example.studentmanagmentrest.service;



import com.example.studentmanagmentrest.model.dto.TeacherDto;
import com.example.studentmanagmentrest.model.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TeacherService {
    String save(String name, String degree);

    Teacher getTeacherByName(String name);

    Teacher findByName(String teacherName);

    List<Teacher> getAll();

    Teacher findByNameAndDegree(String name, String degree);
    boolean teacherExist(String name);

    String updateTeacherDegree(String name, String newDegree);

    String remove(String name);

    boolean purge();

    void save(Teacher teacher);

    Page<TeacherDto> getAllTeachers(int page, int size, Sort.Direction order, String sortField);
}
