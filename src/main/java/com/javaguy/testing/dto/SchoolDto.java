package com.javaguy.testing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolDto {

    private Long id;

    @NotBlank(message = "School name is required")
    @Size(min = 2, max = 100, message = "School name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    private String phoneNumber;

    private Integer studentCount;
}

