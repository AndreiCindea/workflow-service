package com.techquarter.workflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEmployeeRequest {
    @NotBlank
    private String employeeCode;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String costCenter;
}
