package com.javaguy.testing.repository;

import com.javaguy.testing.models.School;
import com.javaguy.testing.models.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    //lest inject the test entity manager
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private StudentRepository studentRepository;

    private Student student1;
    private Student student2;
    private School school;

    @BeforeEach
    void setUp() {
        school = School.builder()
                .address("123 Main St")
                .phoneNumber("123-456-7890")
                .name("Havard")
                .build();
        entityManager.persistAndFlush(school);

        student1 = Student.builder()
                .firstname("John")
                .lastname("Doe")
                .email("johndoe@gmail.com")
                .school(school)
                .build();

        student2 = Student.builder()
                .firstname("Jane")
                .lastname("Smith")
                .email("janesmith@test.com")
                .school(school)
                .build();

        entityManager.persistAndFlush(student1);
        entityManager.persistAndFlush(student2);
    }

    @Test
    @DisplayName("Should find student by email")
    void shouldFindStudentByEmail() {
        // when
        Optional<Student> foundStudent = studentRepository.findByEmail(student1.getEmail());
        assertTrue(foundStudent.isPresent());
        assertEquals(student1, foundStudent.get());
    }

    @Test
    @DisplayName("Should return empty when student not found by email")
    void shouldReturnEmptyWhenStudentNotFoundByEmail() {
        // When
        Optional<Student> found = studentRepository.findByEmail("nonexistent@test.com");
        // Then
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Should find students by school name")
    void shouldFindStudentsBySchoolName() {
        //when
        List<Student> students = studentRepository.findBySchoolName(school.getName());
        //then
        assertEquals(2, students.size());
    assertTrue(students.stream().anyMatch(s -> s.getSchool().getName().equals(school.getName())));
    }

    @Test
    @DisplayName("Should save and retrieve student")
    void shouldSaveAndRetrieveStudent() {
        // Given
        Student newStudent = Student.builder()
                .firstname("Bob")
                .lastname("Wilson")
                .email("bob.wilson@test.com")
                .school(school)
                .build();

        // When
        Student saved = studentRepository.save(newStudent);
        entityManager.flush();
        entityManager.clear(); // Clear persistence context

        Optional<Student> retrieved = studentRepository.findById(saved.getId());

        // Then
        assertTrue(retrieved.isPresent());
        assertEquals("Bob", retrieved.get().getFirstname());
        assertEquals("Wilson", retrieved.get().getLastname());
    }

    @Test
    void shouldDeleteStudent() {
        Long studentId = student1.getId();
        studentRepository.deleteById(studentId);
        entityManager.flush();
        Optional<Student> retrieved = studentRepository.findById(studentId);
        assertTrue(retrieved.isEmpty());
    }
}