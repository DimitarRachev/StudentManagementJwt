package com.example.studentmanagmentrest.service.impl;


import com.example.studentmanagmentrest.model.dto.TeacherDto;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.TeacherService;
import com.example.studentmanagmentrest.utility.DtoConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final ModelMapper mapper;

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, ModelMapper mapper) {
        this.teacherRepository = teacherRepository;
        this.mapper = mapper;
    }

    @Override
    public String save(String name, String degree) {
        if (teacherExist(name)) {
            return "Teacher " + name + " is already in the database";
        } else {
            teacherRepository.save(new Teacher(name, degree));
            return "Teacher " + name + " successfully added to database";
        }
    }

    @Override
    public Teacher getTeacherByName(String name) {
        return teacherRepository.getTeacherByNameAndDeletedFalse(name);
    }

    @Override
    public Teacher findByName(String teacherName) {
        return teacherRepository.findByNameAndDeletedFalse(teacherName);
    }

    @Override
    public List<Teacher> getAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher findByNameAndDegree(String name, String degree) {
        return teacherRepository.findByNameAndDegreeAndDeletedFalse(name, degree);
    }

    @Override
    public boolean teacherExist(String name) {
        return teacherRepository.existsByNameAndDeletedFalse(name);
    }

    @Override
    public String updateTeacherDegree(String name, String newDegree) {
        if (teacherExist(name)) {
            teacherRepository.updateDegree(newDegree, name);
            return name + "'s degree updated to " + newDegree + ".";
        } else {
            return "Teacher " + name + " cannot be found in the database";
        }
    }

    @Override
    public String remove(String name) {
        teacherRepository.deleteByName(name);
        return "Teacher " + name + " successfully removed from the database.";
    }

    @Override
    public boolean purge() {
        teacherRepository.deleteAllByDeletedTrue();
        return true;
    }

    @Override
    public void save(Teacher teacher) {
        teacherRepository.save(teacher); //
    }

    @Override
    public Page<TeacherDto> getAllTeachers(int page, int size, Sort.Direction order, String sortField) {
        Pageable pageable = PageRequest.of(page, size, order, sortField);
        Page<Teacher> all = teacherRepository.findAll(pageable);
        Page<TeacherDto> map = all.map(DtoConverter::makeTeacherDto);
        map.forEach(t -> System.out.println(t.getName() + " " + t.getDegree()));
        return map;
    }
}
