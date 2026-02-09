package com.techquarter.workflow.mapper;

import com.techquarter.workflow.domain.model.Employee;
import com.techquarter.workflow.dto.EmployeeResponse;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .employeeCode(employee.getEmployeeCode())
                .name(employee.getName())
                .email(employee.getEmail())
                .costCenter(employee.getCostCenter())
                .build();
    }
}
