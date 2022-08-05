package com.example.studentmanagmentrest.service.impl;


import com.example.studentmanagmentrest.model.dto.StudentDto;
import com.example.studentmanagmentrest.utility.Message;
import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.repository.GradeRepository;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final ModelMapper mapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, GradeRepository gradeRepository, ModelMapper mapper) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Student student) {
        studentRepository.save(student);
    }

    @Override
    public String getAvgForStudent(String name) {
        Student student = studentRepository.getStudentByNameAndDeletedFalse(name);
        if (student == null) {
            return Message.studentNotExists(name);
        }

        List<Grade> grades = gradeRepository.getAllByStudentAndDeletedFalse(student);
        return String.format("%s's average grade is: %.2f", student.getName(), grades.stream().mapToDouble(Grade::getGrade).average().orElse(0));
    }

    @Override
    public Student getStudentByName(String name) {
        return studentRepository.findByNameAndDeletedFalse(name);
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.getByDeletedFalse();
    }

    @Override
    public Student findByName(String name) {
        return studentRepository.findByNameAndDeletedFalse(name);
    }

    @Override
    public List<Student> getAllByCourse(Course course) {
        return studentRepository.getAllByCoursesContainingAndDeletedFalse(course);
    }

    @Override
    public String updateAge(String name, int newAge) {
        if (studentExists(name)) {
            studentRepository.updateAge(name, newAge);
            return name + "'s age updated to " + newAge + ".";
        } else {
            return Message.studentNotExists(name);
        }
    }

    @Override
    public String add(String name, int age) {
        if (studentExists(name)) {
            return "Student " + name + " is already in database";
        } else {
            studentRepository.save(new Student(name, age));
            return "Student " + name + " successfully added in database";
        }
    }

    @Override
    public boolean studentExists(String name) {
        return studentRepository.existsByNameAndDeletedFalse(name);
    }

    @Override
    public String removeStudentByName(String name) {
        Student student = studentRepository.findByNameAndDeletedFalse(name);
        if (student == null) {
            return Message.studentNotExists(name);
        }

        gradeRepository.deleteGradesByStudent(student);
        studentRepository.deleteByName(name);
        return "Student " + name + " successfully deleted from database";
    }

    @Override
    public boolean purge() {
        studentRepository.deleteAllByDeletedTrue();
        return true;
    }

    @Override
    public Set<Student> getForDeletion() {
        return studentRepository.getByDeletedTrue();
    }

    @Override
    public List<StudentDto> getAllDto() {
        List<Student> students = studentRepository.getByDeletedFalse();

        return students.stream()
                .map(s -> mapper.map(s, StudentDto.class))
                .collect(Collectors.toList());
    }
}
