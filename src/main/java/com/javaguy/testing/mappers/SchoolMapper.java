package com.javaguy.testing.mappers;

import com.javaguy.testing.models.School;
import com.javaguy.testing.dto.SchoolDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SchoolMapper {

    public School toSchool(SchoolDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("SchoolDto cannot be null");
        }

        return School.builder()
                .id(dto.getId())
                .name(dto.getName().trim())
                .address(dto.getAddress().trim())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }

    public SchoolDto toSchoolDto(School school) {
        if (school == null) {
            throw new IllegalArgumentException("School cannot be null");
        }

        return SchoolDto.builder()
                .id(school.getId())
                .name(school.getName())
                .address(school.getAddress())
                .phoneNumber(school.getPhoneNumber())
                .studentCount(school.getStudents() != null ? school.getStudents().size() : 0)
                .build();
    }

    public List<SchoolDto> toSchoolDtoList(List<School> schools) {
        if (schools == null) {
            return List.of();
        }

        return schools.stream()
                .map(this::toSchoolDto)
                .collect(Collectors.toList());
    }
}
