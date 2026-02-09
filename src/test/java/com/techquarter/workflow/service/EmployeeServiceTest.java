package com.techquarter.workflow.service;

import com.techquarter.workflow.domain.model.Employee;
import com.techquarter.workflow.domain.repository.EmployeeRepository;
import com.techquarter.workflow.dto.CreateEmployeeRequest;
import com.techquarter.workflow.dto.EmployeeResponse;
import com.techquarter.workflow.exception.ResourceNotFoundException;
import com.techquarter.workflow.mapper.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private CreateEmployeeRequest validRequest;
    private Employee testEmployee;

    @BeforeEach
    void setUp() {
        validRequest = CreateEmployeeRequest.builder()
                .employeeCode("EMP9876")
                .name("John Doe")
                .email("john@example.com")
                .costCenter("CC-456")
                .build();

        testEmployee = Employee.builder()
                .id(1L)
                .employeeCode("EMP9876")
                .name("John Doe")
                .email("john@example.com")
                .costCenter("CC-456")
                .build();
    }

    @Test
    void testCreateEmployee_Success() {
        // Arrange
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);
        when(employeeMapper.toResponse(testEmployee)).thenReturn(
                EmployeeResponse.builder()
                        .id(1L)
                        .employeeCode("EMP9876")
                        .name("John Doe")
                        .email("john@example.com")
                        .costCenter("CC-456")
                        .build()
        );

        // Act
        EmployeeResponse response = employeeService.createEmployee(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("EMP9876", response.getEmployeeCode());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetEmployee_Success() {
        // Arrange
        when(employeeRepository.findByEmployeeCode("EMP9876"))
                .thenReturn(Optional.of(testEmployee));
        when(employeeMapper.toResponse(testEmployee)).thenReturn(
                EmployeeResponse.builder()
                        .id(1L)
                        .employeeCode("EMP9876")
                        .name("John Doe")
                        .email("john@example.com")
                        .costCenter("CC-456")
                        .build()
        );

        // Act
        EmployeeResponse response = employeeService.getEmployee("EMP9876");

        // Assert
        assertNotNull(response);
        assertEquals("EMP9876", response.getEmployeeCode());
        verify(employeeRepository, times(1)).findByEmployeeCode("EMP9876");
    }

    @Test
    void testGetEmployee_NotFound_ThrowsException() {
        // Arrange
        when(employeeRepository.findByEmployeeCode("NONEXISTENT"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                employeeService.getEmployee("NONEXISTENT"),
                "Should throw exception when employee does not exist");
    }

    @Test
    void testGetEmployeeEntity_Success() {
        // Arrange
        when(employeeRepository.findByEmployeeCode("EMP9876"))
                .thenReturn(Optional.of(testEmployee));

        // Act
        Employee employee = employeeService.getEmployeeEntity("EMP9876");

        // Assert
        assertNotNull(employee);
        assertEquals("EMP9876", employee.getEmployeeCode());
        assertEquals("John Doe", employee.getName());
    }

    @Test
    void testGetEmployeeEntity_NotFound_ThrowsException() {
        // Arrange
        when(employeeRepository.findByEmployeeCode("NONEXISTENT"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                employeeService.getEmployeeEntity("NONEXISTENT"),
                "Should throw exception when employee does not exist");
    }
}
