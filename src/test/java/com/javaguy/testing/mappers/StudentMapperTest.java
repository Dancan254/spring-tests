package com.javaguy.testing.mappers;

import com.javaguy.testing.dto.StudentDto;
import com.javaguy.testing.models.School;
import com.javaguy.testing.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private StudentMapper studentMapper;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentMapper();
    }

    @Test
    @DisplayName("Should map StudentDto to Student")
    void shouldMapStudentDtoToStudent() {
        // what are we given?
        StudentDto dto = StudentDto.builder()
                .firstname("John")
                .lastname("Doe")
                .email("johndoe@gmail.com")
                .schoolName("MIT")
                .build();
        // when what?
        Student student = studentMapper.toStudent(dto);

        //then?
        assertAll(
                () -> assertNotNull(student),
                () -> assertEquals(dto.getFirstname(), student.getFirstname()),
                () -> assertEquals(dto.getLastname(), student.getLastname()),
                () -> assertEquals(dto.getEmail(), student.getEmail()),
                () -> assertNotNull(student.getSchool()),
                () -> assertEquals(dto.getSchoolName(), student.getSchool().getName())
        );
    }

    @Test
    @DisplayName("Should map student to StudentDto")
    void shouldMapStudentToStudentDto(){
        // given
        School school =  School.builder()
                .name("Havard")
                .build();
        Student student = Student.builder()
                .firstname("John")
                .lastname("Doe")
                .email("")
                .school(school)
                .build();

        //when
        StudentDto dto = studentMapper.toStudentDto(student);

        assertAll(
                () -> assertNotNull(dto),
                () -> assertEquals(student.getFirstname(), dto.getFirstname()),
                () -> assertEquals(student.getLastname(), dto.getLastname()),
                () -> assertEquals(student.getEmail(), dto.getEmail()),
                () -> assertEquals(student.getSchool().getName(), dto.getSchoolName())
        );
    }

    @Test
    @DisplayName("should handle null values gracefully")
    void shouldHandleNullValues(){
        StudentDto dto = null;

        assertThrows(IllegalArgumentException.class, () -> studentMapper.toStudent(dto));
    }
}