package com.example.studentmanagmentrest.controller;

import com.example.studentmanagmentrest.model.binding.CourseBindingDto;
import com.example.studentmanagmentrest.model.dto.CourseDto;
import com.example.studentmanagmentrest.model.dto.CourseDtoWithGrades;
import com.example.studentmanagmentrest.model.dto.StudentDtoAvgGrade;
import com.example.studentmanagmentrest.model.entity.Teacher;
import com.example.studentmanagmentrest.service.DBFacade;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CourseRestControllerTest {
    @MockBean
   private DBFacade dbFacade;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "MANAGER")
    void getCoursesDtoFail() throws Exception {

        when(dbFacade.getCoursesDto()).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/allWithoutGrades"))
                .andExpect(status().is5xxServerError());
    }

 @Test
 @WithMockUser(roles = "MANAGER")
 void getCoursesDtoSuccess() throws Exception {
  List<CourseDto> coursesDto = new ArrayList<>();
  CourseDto dto = new CourseDto("name", new HashSet<>(), new Teacher());
  coursesDto.add(dto);
  when(dbFacade.getCoursesDto()).thenReturn(coursesDto);

  mockMvc.perform(MockMvcRequestBuilders.get("/courses/allWithoutGrades"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(1));
 }

    @Test
    @WithMockUser(roles = "MANAGER")
    void getCoursesDtoWithGrades() throws Exception {
        CourseDtoWithGrades courseDtoWithGrades = new CourseDtoWithGrades();
        ArrayList<CourseDtoWithGrades> list = new ArrayList<>();
        list.add(courseDtoWithGrades);
        when(dbFacade.getCoursesDtoWithGrades()).thenReturn(list);
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/allWithGrades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addCourseSuccess() throws Exception {
        Gson gson = new Gson();
        CourseBindingDto course = new CourseBindingDto();
        course.setName("Java");
        course.setDuration(100);
        when(dbFacade.addNewCourse("Java", 100)).thenReturn("Course Java saved successfully.");
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(course)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Course Java saved successfully."));

    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addCourseFail() throws Exception {
        Gson gson = new Gson();
        CourseBindingDto course = new CourseBindingDto();
        course.setName("Java");
        course.setDuration(100);
        when(dbFacade.addNewCourse("Java", 100)).thenReturn("Course Java already in database.");
        mockMvc.perform(MockMvcRequestBuilders.post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(course)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Course Java already in database."));

    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addTeacherSuccess() throws Exception {
        when(dbFacade.addTeacherToCourse("SomeTeacher", "Java"))
                .thenReturn("Teacher SomeTeacher now in charge for course Java");
        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/Java/addTeacher/SomeTeacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Teacher SomeTeacher now in charge for course Java"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addTeacherNoTeacher() throws Exception {
        when(dbFacade.addTeacherToCourse("SomeTeacher", "Java"))
                .thenReturn("Teacher SomeTeacher cannot be found in the database.");
        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/Java/addTeacher/SomeTeacher"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Teacher SomeTeacher cannot be found in the database."));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addStudentSuccess() throws Exception {
        when(dbFacade.addStudentToCourse("Me", "Java"))
                .thenReturn(" Student Me successfully enrolled in course Java.");
        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/Java/addStudent/Me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Student Me successfully enrolled in course Java."));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void addStudentFail() throws Exception {
        when(dbFacade.addStudentToCourse("Me", "Java"))
                .thenReturn("Student Me cannot be found in the database.");
        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/Java/addStudent/Me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Student Me cannot be found in the database."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteCourseSuccess() throws Exception {
        when(dbFacade.removeCourseByName("Java")).thenReturn("Course Java successfully removed from the database.");
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Course Java successfully removed from the database."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteCourseFail() throws Exception {
        when(dbFacade.removeCourseByName("Java")).thenReturn("Course Java cannot be found in the database.");
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/Java"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Course Java cannot be found in the database."));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void removeStudentFromCourseSuccess() throws Exception {
        when(dbFacade.removeStudentFromCourse("Me", "Java"))
                .thenReturn("Student Me successfully removed from Java course.");

        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/Java/removeStudent/Me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Student Me successfully removed from Java course."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void removeStudentFromCourseFail() throws Exception {
        when(dbFacade.removeStudentFromCourse("Me", "Java"))
                .thenReturn("Course Java cannot be found in the database");

        mockMvc.perform(MockMvcRequestBuilders.patch("/courses/Java/removeStudent/Me"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Course Java cannot be found in the database"));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateCourseDurationSuccess() throws Exception {
        Gson gson = new Gson();
        CourseBindingDto course =new CourseBindingDto();
        course.setName("Java");
        course.setDuration(100);
        when(dbFacade.updateCourseDuration("Java", 100))
                .thenReturn("Java's duration changed to 100 hours.");

        mockMvc.perform(MockMvcRequestBuilders.patch("/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(course)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Java's duration changed to 100 hours."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateCourseDurationFail() throws Exception {
        Gson gson = new Gson();
        CourseBindingDto course =new CourseBindingDto();
        course.setName("Java");
        course.setDuration(100);
        when(dbFacade.updateCourseDuration("Java", 100))
                .thenReturn("Course Java cannot be found in the database.");

        mockMvc.perform(MockMvcRequestBuilders.patch("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(course)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Course Java cannot be found in the database."));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getAvgForCourseSuccess() throws Exception {
       StudentDtoAvgGrade student = new StudentDtoAvgGrade();
       List<StudentDtoAvgGrade> students = new ArrayList<>();
       students.add(student);
        when(dbFacade.getAvgForStudentsInCourse("Java"))
                .thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    void getAvgForCourseFail() throws Exception {

        when(dbFacade.getAvgForStudentsInCourse("Java"))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/Java"))
                .andExpect(status().isNoContent());
    }
}