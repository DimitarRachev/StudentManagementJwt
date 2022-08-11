package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.enumeration.UserRole;
import com.example.studentmanagmentrest.model.binding.RegistrationRequest;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.model.entity.User;
import com.example.studentmanagmentrest.model.entity.UserEntity;
import com.example.studentmanagmentrest.repository.StudentRepository;
import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.EmailService;
import com.example.studentmanagmentrest.service.RegistrationService;
import com.example.studentmanagmentrest.service.UserService;
import com.example.studentmanagmentrest.utility.EmailValidator;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final EmailValidator emailValidator;
    private final EmailService emailService;
    private final Duration tokenDuration = Duration.ofHours(1);

    private final String BASE_REGISTER = "http://localhost:8008/register/confirm?user=%s&token=%s";
    private final String BASE_REFRESH = "http://localhost:8008/register/refresh?user=%s&token=%s";


    @Override
    public String register(RegistrationRequest request) {
        boolean isValid = emailValidator.validate(request.getEmail());
        if (!isValid) {
            throw new IllegalStateException("Invalid email address: " + request.getEmail());
        }

        String token = UUID.randomUUID().toString();
        User registeredUser = null;
        Optional<Student> registeredStudent = studentRepository.findByUsernameOrEmailAndDeletedFalse(request.getUsername(), request.getPassword());
        if (registeredStudent.isPresent()) {
            registeredUser = new User(registeredStudent.get());
        }
        Optional<Teacher> registeredTeacher = teacherRepository.findByUsernameOrEmailAndDeletedFalse(request.getUsername(), request.getEmail());
        if (registeredTeacher.isPresent()) {
            registeredUser = new User(registeredTeacher.get());
        }

        if (registeredUser != null && registeredUser.isEnabled()) {
            throw new IllegalStateException("Username or email already in use.");
        }

        Student student = new Student();
        student.setAge(request.getAge())
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setEnabled(false)
                .setRoles(Sets.newHashSet(UserRole.STUDENT))
                .setRegisterConfirmToken(token)
                .setRegisterTockenExpiratisAt(LocalDateTime.now().plus(tokenDuration))
                .setName(request.getUsername());
        emailService.send(student.getEmail(), buildEmail(student.getName(), getLink(BASE_REGISTER, student)));
        studentRepository.save(student);
        return "User " + student.getUsername() + " registered. Token: " + student.getRegisterConfirmToken();
    }

    private String getLink(String baseLink, UserEntity user) {
        return String.format(baseLink, user.getUsername(), user.getRegisterConfirmToken());
    }


    @Override
    public String confirm(String token, String name) {
        Optional<Teacher> optionalTeacher = teacherRepository.findByUsernameOrEmailAndDeletedFalse(name, name);
        Optional<Student> optionalStudent = studentRepository.findByUsernameOrEmailAndDeletedFalse(name, name);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getRegisterTockenExpiratisAt().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Token has expired!");
            }
            if (!student.getRegisterConfirmToken().equals(token)) {
                throw new IllegalStateException("Token is invalid!");
            }
            student.setEnabled(true);
            studentRepository.save(student);

        } else if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            if (teacher.getRegisterTockenExpiratisAt().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Token has expired!");
            }
            if (!teacher.getRegisterConfirmToken().equals(token)) {
                throw new IllegalStateException("Token is invalid!");
            }
            teacher.setEnabled(true);
            teacherRepository.save(teacher);
        } else {
            throw new IllegalStateException("User " + name + " not found!");
        }
        return "Your email has been successfully confirmed.";
    }

    @Override
    public String getNewToken(String name) {
        Optional<Teacher> optionalTeacher = teacherRepository.findByUsernameOrEmailAndDeletedFalse(name, name);
        Optional<Student> optionalStudent = studentRepository.findByUsernameOrEmailAndDeletedFalse(name, name);
        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plus(tokenDuration);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setRegisterConfirmToken(token);
            student.setRegisterTockenExpiratisAt(expiration);
            emailService.send(student.getEmail(), buildEmail(student.getName(), getLink(BASE_REGISTER,student)));
            studentRepository.save(student);
        } else if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            teacher.setRegisterConfirmToken(token);
            teacher.setRegisterTockenExpiratisAt(expiration);
            emailService.send(teacher.getEmail(), buildEmail(teacher.getName(), getLink(BASE_REGISTER, teacher)));
            teacherRepository.save(teacher);
        } else {
            throw new IllegalStateException("User not found.");
        }
        return "New confirmation email has been send. token: " + token;
    }


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 60 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
