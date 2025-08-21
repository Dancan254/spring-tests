package com.javaguy.testing.service;

import com.javaguy.testing.exception.SchoolAlreadyExistsException;
import com.javaguy.testing.exception.SchoolNotFoundException;
import com.javaguy.testing.models.School;
import com.javaguy.testing.dto.SchoolDto;
import com.javaguy.testing.mappers.SchoolMapper;
import com.javaguy.testing.repository.SchoolRepository;
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
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final SchoolMapper schoolMapper;

    @Transactional
    public SchoolDto createSchool(SchoolDto schoolDto) {
        log.info("Creating school: {}", schoolDto.getName());

        if (schoolRepository.existsByName(schoolDto.getName())) {
            throw new SchoolAlreadyExistsException("School with name " + schoolDto.getName() + " already exists");
        }

        School school = schoolMapper.toSchool(schoolDto);
        School savedSchool = schoolRepository.save(school);

        log.info("Successfully created school with ID: {}", savedSchool.getId());
        return schoolMapper.toSchoolDto(savedSchool);
    }

    public School findByName(String name) {
        return schoolRepository.findByName(name)
                .orElseThrow(() -> new SchoolNotFoundException("School not found with name: " + name));
    }

    public Optional<SchoolDto> findSchoolDtoByName(String name) {
        return schoolRepository.findByName(name)
                .map(schoolMapper::toSchoolDto);
    }

    public Optional<SchoolDto> findById(Long id) {
        return schoolRepository.findById(id)
                .map(schoolMapper::toSchoolDto);
    }

    public List<SchoolDto> findAll() {
        List<School> schools = schoolRepository.findAll();
        return schoolMapper.toSchoolDtoList(schools);
    }

    @Transactional
    public void deleteSchool(Long id) {
        log.info("Deleting school with ID: {}", id);

        if (!schoolRepository.existsById(id)) {
            throw new SchoolNotFoundException("School not found with ID: " + id);
        }

        schoolRepository.deleteById(id);
        log.info("Successfully deleted school with ID: {}", id);
    }
}
