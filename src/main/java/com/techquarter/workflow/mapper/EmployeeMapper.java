package com.techquarter.workflow.mapper;

import com.techquarter.workflow.domain.model.Employee;
import com.techquarter.workflow.dto.EmployeeResponse;

public class EmployeeMapper {

    public static EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employeeCode(employee.getEmployeeCode())
                .name(employee.getName())
                .email(employee.getEmail())
                .costCenter(employee.getCostCenter())
                .build();
    }
}
