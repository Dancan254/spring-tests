package com.javaguy.testing.controller;

import com.javaguy.testing.exception.StudentNotFoundException;
import com.javaguy.testing.dto.StudentDto;
import com.javaguy.testing.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        log.info("Request to create models: {}", studentDto.getEmail());

        StudentDto createdStudent = studentService.createStudent(studentDto);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        log.debug("Request to get models by ID: {}", id);

        return studentService.findById(id)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StudentDto> getStudentByEmail(@PathVariable String email) {
        log.debug("Request to get models by email: {}", email);

        return studentService.findByEmail(email)
                .map(student -> ResponseEntity.ok(student))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        log.debug("Request to get all students");

        List<StudentDto> students = studentService.findAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/school/{schoolName}")
    public ResponseEntity<List<StudentDto>> getStudentsBySchool(@PathVariable String schoolName) {
        log.debug("Request to get students by school: {}", schoolName);

        List<StudentDto> students = studentService.findBySchoolName(schoolName);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDto>> searchStudents(@RequestParam String name) {
        log.debug("Request to search students by name: {}", name);

        List<StudentDto> students = studentService.searchByName(name);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id,
                                                    @Valid @RequestBody StudentDto studentDto) {
        log.info("Request to update models with ID: {}", id);

        try {
            StudentDto updatedStudent = studentService.updateStudent(id, studentDto);
            return ResponseEntity.ok(updatedStudent);
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        log.info("Request to delete models with ID: {}", id);

        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (StudentNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
