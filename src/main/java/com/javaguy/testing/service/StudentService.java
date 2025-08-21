package com.javaguy.testing.service;

import com.javaguy.testing.exception.StudentAlreadyExistsException;
import com.javaguy.testing.exception.StudentNotFoundException;
import com.javaguy.testing.models.School;
import com.javaguy.testing.models.Student;
import com.javaguy.testing.dto.StudentDto;
import com.javaguy.testing.mappers.StudentMapper;
import com.javaguy.testing.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolService schoolService;
    private final StudentMapper studentMapper;

    @Transactional
    public StudentDto createStudent(StudentDto studentDto) {
        log.info("Creating models with email: {}", studentDto.getEmail());

        // Check if models already exists
        if (studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new StudentAlreadyExistsException("Student with email " + studentDto.getEmail() + " already exists");
        }

        // Find or create school
        School school = schoolService.findByName(studentDto.getSchoolName());

        // Map DTO to entity
        Student student = studentMapper.toStudent(studentDto);
        student.setSchool(school);

        // Save models
        Student savedStudent = studentRepository.save(student);

        log.info("Successfully created models with ID: {}", savedStudent.getId());
        return studentMapper.toStudentDto(savedStudent);
    }

    public Optional<StudentDto> findByEmail(String email) {
        log.debug("Finding models by email: {}", email);

        return studentRepository.findByEmail(email)
                .map(studentMapper::toStudentDto);
    }

    public Optional<StudentDto> findById(Long id) {
        log.debug("Finding models by ID: {}", id);

        return studentRepository.findById(id)
                .map(studentMapper::toStudentDto);
    }

    public List<StudentDto> findAll() {
        log.debug("Finding all students");

        List<Student> students = studentRepository.findAll();
        return studentMapper.toStudentDtoList(students);
    }

    public List<StudentDto> findBySchoolName(String schoolName) {
        log.debug("Finding students by school name: {}", schoolName);

        List<Student> students = studentRepository.findBySchoolName(schoolName);
        return studentMapper.toStudentDtoList(students);
    }

    public List<StudentDto> searchByName(String name) {
        log.debug("Searching students by name: {}", name);

        List<Student> students = studentRepository.findByNameContaining(name);
        return studentMapper.toStudentDtoList(students);
    }

    @Transactional
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        log.info("Updating models with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));

        // Check if email is being changed and if new email already exists
        if (!existingStudent.getEmail().equals(studentDto.getEmail()) &&
                studentRepository.existsByEmail(studentDto.getEmail())) {
            throw new StudentAlreadyExistsException("Student with email " + studentDto.getEmail() + " already exists");
        }

        // Update school if changed
        if (studentDto.getSchoolName() != null &&
                !studentDto.getSchoolName().equals(existingStudent.getSchool().getName())) {
            School newSchool = schoolService.findByName(studentDto.getSchoolName());
            existingStudent.setSchool(newSchool);
        }

        // Update models fields
        studentMapper.updateStudentFromDto(studentDto, existingStudent);

        Student updatedStudent = studentRepository.save(existingStudent);

        log.info("Successfully updated models with ID: {}", id);
        return studentMapper.toStudentDto(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        log.info("Deleting models with ID: {}", id);

        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student not found with ID: " + id);
        }

        studentRepository.deleteById(id);
        log.info("Successfully deleted models with ID: {}", id);
    }

    @Transactional
    public void deleteByEmail(String email) {
        log.info("Deleting models with email: {}", email);

        if (!studentRepository.existsByEmail(email)) {
            throw new StudentNotFoundException("Student not found with email: " + email);
        }

        studentRepository.deleteByEmail(email);
        log.info("Successfully deleted models with email: {}", email);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    public long countStudentsBySchool(Long schoolId) {
        return studentRepository.countBySchoolId(schoolId);
    }
}
