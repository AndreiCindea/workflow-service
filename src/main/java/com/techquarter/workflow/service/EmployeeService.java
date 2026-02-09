package com.techquarter.workflow.service;

import com.techquarter.workflow.domain.model.Employee;
import com.techquarter.workflow.domain.repository.EmployeeRepository;
import com.techquarter.workflow.dto.CreateEmployeeRequest;
import com.techquarter.workflow.dto.EmployeeResponse;
import com.techquarter.workflow.exception.ResourceNotFoundException;
import com.techquarter.workflow.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        Employee employee = Employee.builder()
                .employeeCode(request.getEmployeeCode())
                .name(request.getName())
                .email(request.getEmail())
                .costCenter(request.getCostCenter())
                .build();

        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponse(saved);
    }

    public EmployeeResponse getEmployee(String employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee with code " + employeeCode + " not found"));
        return employeeMapper.toResponse(employee);
    }

    public Employee getEmployeeEntity(String employeeCode) {
        return employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee with code " + employeeCode + " not found"));
    }
}
