package com.example.studentmanagmentrest.service.impl;

import com.example.studentmanagmentrest.repository.TeacherRepository;
import com.example.studentmanagmentrest.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherServiceImplTest {
    TeacherRepository mockedTeacherRepository;
    TeacherService teacherService;

    @BeforeEach
    void setUp() {
        mockedTeacherRepository = mock(TeacherRepository.class);
        teacherService = new TeacherServiceImpl(mockedTeacherRepository);
    }

    @Test
    void save() {
        when(mockedTeacherRepository.existsByNameAndDeletedFalse("notExisting")).thenReturn(false);
        when(mockedTeacherRepository.existsByNameAndDeletedFalse("existing")).thenReturn(true);
        assertAll(
                () -> assertEquals("Teacher existing is already in the database",
                        teacherService.save("existing", "degree")),
                () -> assertEquals("Teacher notExisting successfully added to database",
                        teacherService.save("notExisting", "degree"))
        );
    }


    @Test
    void updateTeacherDegree() {
        when(mockedTeacherRepository.existsByNameAndDeletedFalse("deleted")).thenReturn(false);
        when(mockedTeacherRepository.existsByNameAndDeletedFalse("existing")).thenReturn(true);
        assertAll(
                () -> assertEquals("Teacher deleted cannot be found in the database",
                        teacherService.updateTeacherDegree("deleted", "degree")),
                () -> assertEquals("existing's degree updated to degree.",
                        teacherService.updateTeacherDegree("existing", "degree"))
        );
    }

    @Test
    void remove() {
        assertEquals("Teacher name successfully removed from the database.",
                teacherService.remove("name"));
    }

}