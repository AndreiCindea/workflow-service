package com.techquarter.workflow.service;

import com.techquarter.workflow.domain.model.*;
import com.techquarter.workflow.domain.repository.BookingRepository;
import com.techquarter.workflow.dto.BookingResponse;
import com.techquarter.workflow.dto.CreateBookingRequest;
import com.techquarter.workflow.exception.ResourceNotFoundException;
import com.techquarter.workflow.mapper.BookingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    private CreateBookingRequest validRequest;
    private Employee testEmployee;
    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testEmployee = Employee.builder()
                .id(1L)
                .employeeCode("EMP9876")
                .name("John Doe")
                .email("john@example.com")
                .costCenter("CC-456")
                .build();

        LocalDateTime departureDate = LocalDateTime.of(2024, 11, 5, 8, 0);
        LocalDateTime returnDate = LocalDateTime.of(2024, 11, 8, 18, 0);

        validRequest = CreateBookingRequest.builder()
                .employeeCode("EMP9876")
                .resourceType(ResourceType.FLIGHT)
                .destination("NYC")
                .departureDate(departureDate)
                .returnDate(returnDate)
                .travelerCount(1)
                .costCenterRef("CC-456")
                .tripPurpose("Client meeting - Acme Corp")
                .build();

        testBooking = Booking.builder()
                .id(1L)
                .employee(testEmployee)
                .resourceType(ResourceType.FLIGHT)
                .destination("NYC")
                .departureDate(departureDate)
                .returnDate(returnDate)
                .travelerCount(1)
                .costCenterRef("CC-456")
                .tripPurpose("Client meeting - Acme Corp")
                .status(BookingStatus.CREATED)
                .build();
    }

    @Test
    void testCreateBooking_Success() {
        // Arrange
        when(employeeService.getEmployeeEntity("EMP9876")).thenReturn(testEmployee);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        when(bookingMapper.toResponse(testBooking)).thenReturn(
                BookingResponse.builder()
                        .id(1L)
                        .employeeCode("EMP9876")
                        .employeeName("John Doe")
                        .resourceType(ResourceType.FLIGHT)
                        .destination("NYC")
                        .departureDate(validRequest.getDepartureDate())
                        .returnDate(validRequest.getReturnDate())
                        .travelerCount(1)
                        .costCenterRef("CC-456")
                        .tripPurpose("Client meeting - Acme Corp")
                        .status(BookingStatus.CREATED)
                        .build()
        );

        // Act
        BookingResponse response = bookingService.createBooking(validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("EMP9876", response.getEmployeeCode());
        assertEquals("NYC", response.getDestination());
        assertEquals(BookingStatus.CREATED, response.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_InvalidDates_ThrowsException() {
        // Arrange
        LocalDateTime departureDate = LocalDateTime.of(2024, 11, 8, 18, 0);
        LocalDateTime returnDate = LocalDateTime.of(2024, 11, 5, 8, 0);

        CreateBookingRequest invalidRequest = CreateBookingRequest.builder()
                .employeeCode("EMP9876")
                .resourceType(ResourceType.FLIGHT)
                .destination("NYC")
                .departureDate(departureDate)
                .returnDate(returnDate)
                .travelerCount(1)
                .costCenterRef("CC-456")
                .tripPurpose("Client meeting - Acme Corp")
                .build();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(invalidRequest),
                "Should throw exception when return date is before departure date");
    }

    @Test
    void testCreateBooking_EmployeeNotFound_ThrowsException() {
        // Arrange
        when(employeeService.getEmployeeEntity("NONEXISTENT"))
                .thenThrow(new ResourceNotFoundException("Employee not found"));

        CreateBookingRequest requestWithInvalidEmployee = CreateBookingRequest.builder()
                .employeeCode("NONEXISTENT")
                .resourceType(ResourceType.FLIGHT)
                .destination("NYC")
                .departureDate(validRequest.getDepartureDate())
                .returnDate(validRequest.getReturnDate())
                .travelerCount(1)
                .costCenterRef("CC-456")
                .tripPurpose("Client meeting")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.createBooking(requestWithInvalidEmployee),
                "Should throw exception when employee does not exist");
    }
}
