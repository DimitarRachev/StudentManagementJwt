package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.model.entity.User;
import com.example.studentmanagmentrest.model.entity.UserEntity;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public UserServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> byUsernameAndDeletedFalse = studentRepository.findByUsernameAndDeletedFalse(username);
        Optional<UserEntity> byUsernameAndDeletedFalse1 = teacherRepository.findByUsernameAndDeletedFalse(username);
        UserEntity userEntity = byUsernameAndDeletedFalse
                .orElse(byUsernameAndDeletedFalse1
                        .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found")));
        return new User(userEntity);
    }
}
