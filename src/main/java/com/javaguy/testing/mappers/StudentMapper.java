package com.javaguy.testing.mappers;

import com.javaguy.testing.models.School;
import com.javaguy.testing.models.Student;
import com.javaguy.testing.dto.StudentDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public Student toStudent(StudentDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("StudentDto cannot be null");
        }

        validateStudentDto(dto);
        var student = Student.builder()
                .firstname(dto.getFirstname().trim())
                .lastname(dto.getLastname().trim())
                .email(dto.getEmail().toLowerCase().trim())
                .build();
        var school = dto.getSchoolName() != null ?
                School.builder().name(dto.getSchoolName().trim()).build() : null;
        student.setSchool(school);
        return student;
    }

    public StudentDto toStudentDto(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }

        return StudentDto.builder()
                .id(student.getId())
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .email(student.getEmail())
                .schoolName(student.getSchool() != null ? student.getSchool().getName() : null)
                .createdAt(student.getCreatedAt())
                .build();
    }

    public List<StudentDto> toStudentDtoList(List<Student> students) {
        if (students == null) {
            return List.of();
        }

        return students.stream()
                .map(this::toStudentDto)
                .collect(Collectors.toList());
    }

    public void updateStudentFromDto(StudentDto dto, Student student) {
        if (dto == null || student == null) {
            throw new IllegalArgumentException("StudentDto and Student cannot be null");
        }

        validateStudentDto(dto);

        if (dto.getFirstname() != null) {
            student.setFirstname(dto.getFirstname().trim());
        }
        if (dto.getLastname() != null) {
            student.setLastname(dto.getLastname().trim());
        }
        if (dto.getEmail() != null) {
            student.setEmail(dto.getEmail().toLowerCase().trim());
        }
    }

    private void validateStudentDto(StudentDto dto) {
        if (dto.getFirstname() == null || dto.getFirstname().trim().isEmpty()) {
            throw new IllegalArgumentException("Firstname cannot be null or empty");
        }
        if (dto.getLastname() == null || dto.getLastname().trim().isEmpty()) {
            throw new IllegalArgumentException("Lastname cannot be null or empty");
        }
        if (dto.getEmail() == null || !isValidEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email must be valid");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null &&
                !email.trim().isEmpty() &&
                email.contains("@") &&
                email.contains(".") &&
                email.indexOf("@") < email.lastIndexOf(".");
    }
}
