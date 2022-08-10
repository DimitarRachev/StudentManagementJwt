package com.example.studentmanagmentrest.utility;



import com.example.studentmanagmentrest.model.entity.Course;
import com.example.studentmanagmentrest.model.entity.Grade;
import com.example.studentmanagmentrest.model.entity.Student;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.model.dto.TeacherDto;
import com.example.studentmanagmentrest.service.GradeService;
import com.example.studentmanagmentrest.model.dto.CourseDto;
import com.example.studentmanagmentrest.model.dto.CourseDtoWithGrades;
import com.example.studentmanagmentrest.model.dto.StudentDto;
import com.example.studentmanagmentrest.model.dto.StudentDtoAvgGrade;
import com.example.studentmanagmentrest.model.dto.StudentDtoWithGrades;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoConverter {

    public static CourseDtoWithGrades makeCourseDtoWithGrades(Course course, GradeService gradeService) {
        CourseDtoWithGrades toReturn = new CourseDtoWithGrades();
        toReturn.setName(course.getName());
        List<Student> students = course.getStudents().stream().collect(Collectors.toList());
        List<StudentDtoWithGrades> studentDtoWithGrades = new ArrayList<>();
        for (Student student : students) {
            StudentDtoWithGrades temp = new StudentDtoWithGrades();
            temp.setName(student.getName());
            List<Grade> gradeServiceByStudentAndCourse = gradeService.findByStudentAndCourse(student, course);
            temp.setGrades(gradeServiceByStudentAndCourse.stream().map(Grade::getGrade).toList());
            studentDtoWithGrades.add(temp);
        }
        studentDtoWithGrades.sort(Comparator.comparing(s -> s.getGrades().stream().mapToDouble(e -> e).average().orElse(0)));
        toReturn.setStudents(studentDtoWithGrades);
        return toReturn;
    }

    public static StudentDtoAvgGrade makeAvgGradeStudent(Student student, GradeService gradeService, Course course) {
        StudentDtoAvgGrade temp = new StudentDtoAvgGrade();
        temp.setName(student.getName());
        temp.setAvgGrade(gradeService.findByStudentAndCourse(student, course).stream().mapToDouble(Grade::getGrade).average().orElse(0));
        return temp;
    }

    public static CourseDto makeCoursesDto(Course c) {
        CourseDto dto = new CourseDto();
        Teacher teacher = c.getTeacher();
        if (teacher != null && !teacher.deleted()) {
            dto.setTeacher(teacher);
        }
        dto.setName(c.getName());
        Set<StudentDto> students = new LinkedHashSet<>();
        for (Student s : c.getStudents()) {
            if (!s.deleted()) {
                StudentDto student = new StudentDto();
                student.setName(s.getName());
                students.add(student);
            }
        }
        dto.setStudents(students);
        return dto;
    }

    public static TeacherDto makeTeacherDto(Teacher teacher) {
        TeacherDto dto = new TeacherDto();
        dto.setName(teacher.getName())
                .setDegree(teacher.getDegree());
        return dto;
    }
}
