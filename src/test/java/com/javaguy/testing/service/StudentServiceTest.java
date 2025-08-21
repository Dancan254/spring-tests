package com.javaguy.testing.service;

import com.javaguy.testing.dto.StudentDto;
import com.javaguy.testing.mappers.StudentMapper;
import com.javaguy.testing.models.School;
import com.javaguy.testing.models.Student;
import com.javaguy.testing.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Here we will use Mockito to mock the repository and the mapper and any other required dependencies.
 * later on inject the mocks into the service class.
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    SchoolService schoolService;

    @InjectMocks
    private StudentService studentService;

    private StudentDto studentDto;
    private Student student;
    private School school;


    @BeforeEach
    void setUp() {

        school = School.builder()
                .id(1L)
                .name("Test University")
                .build();

        student = Student.builder()
                .id(1L)
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@test.com")
                .school(school)
                .build();

        studentDto = StudentDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@test.com")
                .schoolName("Test University")
                .build();
    }

    @Test
    void shouldCreateStudent() {
        // Given - Setup mock behaviors
        when(studentRepository.existsByEmail("john.doe@test.com")).thenReturn(false);
        when(schoolService.findByName("Test University")).thenReturn(school);
        when(studentMapper.toStudent(studentDto)).thenReturn(student);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentMapper.toStudentDto(student)).thenReturn(studentDto);

        // When
        StudentDto result = studentService.createStudent(studentDto);

        // Then - Assertions
        assertNotNull(result);
        assertEquals("John", result.getFirstname());
        assertEquals("Doe", result.getLastname());
        assertEquals("john.doe@test.com", result.getEmail());
        assertEquals("Test University", result.getSchoolName());

        // Verify interactions - Verify in the order they're called
        verify(studentRepository).existsByEmail("john.doe@test.com");
        verify(schoolService).findByName("Test University");
        verify(studentMapper).toStudent(studentDto);
        verify(studentRepository).save(any(Student.class));
        verify(studentMapper).toStudentDto(student);
    }
}