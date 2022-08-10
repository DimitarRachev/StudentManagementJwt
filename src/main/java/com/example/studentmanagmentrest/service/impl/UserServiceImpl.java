package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.model.entity.User;
import com.example.studentmanagmentrest.model.entity.UserEntity;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.LoginAttemptService;
import com.example.studentmanagmentrest.service.UserService;
import org.aspectj.bridge.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final LoginAttemptService loggingAttemptService;

    @Autowired
    public UserServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository, LoginAttemptService loggingAttemptService) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.loggingAttemptService = loggingAttemptService;
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
        
        valideteLoginAttempt(userEntity);
        return new User(userEntity);
        }


    private void valideteLoginAttempt(UserEntity user)  {
        if(user.isAccountNonLocked()){
            if(loggingAttemptService.hasExceededMaxAttempts(user.getUsername())){
                user.setAccountNonLocked(false);
            }else {
                user.setAccountNonLocked(true);
            }
        }
        else {
            loggingAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }
}
