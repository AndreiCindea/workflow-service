package com.techquarter.workflow.controller;

import com.techquarter.workflow.dto.CreateEmployeeRequest;
import com.techquarter.workflow.dto.EmployeeResponse;
import com.techquarter.workflow.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{employeeCode}")
    public ResponseEntity<EmployeeResponse> getEmployee(
            @PathVariable String employeeCode) {
        EmployeeResponse response = employeeService.getEmployee(employeeCode);
        return ResponseEntity.ok(response);
    }
}
