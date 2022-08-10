package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.model.entity.User;
import com.example.studentmanagmentrest.model.entity.UserEntity;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public UserServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity;
        Optional<UserEntity> student = studentRepository.findByUsernameAndDeletedFalse(username);
        Optional<UserEntity> teacher = teacherRepository.findByUsernameAndDeletedFalse(username);
        if (student.isPresent()) {
            userEntity = student.get();
        } else if (teacher.isPresent()) {
            userEntity = teacher.get();
        } else {
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        return new User(userEntity);
    }
}
