package com.techquarter.workflow.controller;

import com.techquarter.workflow.domain.model.BookingStatus;
import com.techquarter.workflow.domain.model.ResourceType;
import com.techquarter.workflow.dto.BookingResponse;
import com.techquarter.workflow.dto.CreateBookingRequest;
import com.techquarter.workflow.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerIntegrationTest {

    @Mock
    private BookingService bookingService;


    @InjectMocks
    private BookingController bookingController;

    private CreateBookingRequest validRequest;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        LocalDateTime departure = LocalDateTime.of(2024, 12, 15, 10, 0);
        LocalDateTime returnDate = LocalDateTime.of(2024, 12, 20, 18, 0);

        validRequest = CreateBookingRequest.builder()
                .employeeCode("EMP_INT_001")
                .resourceType(ResourceType.FLIGHT)
                .destination("NYC")
                .departureDate(departure)
                .returnDate(returnDate)
                .travelerCount(2)
                .costCenterRef("CC-INT-001")
                .tripPurpose("Annual Conference")
                .build();


        bookingResponse = BookingResponse.builder()
                .id(1L)
                .employeeCode("EMP_INT_001")
                .employeeName("Integration Test Employee")
                .resourceType(ResourceType.FLIGHT)
                .destination("NYC")
                .departureDate(departure)
                .returnDate(returnDate)
                .travelerCount(2)
                .costCenterRef("CC-INT-001")
                .tripPurpose("Annual Conference")
                .status(BookingStatus.CREATED)
                .build();
    }

    @Test
    void testCreateBooking_SuccessfulFlow() {
        // Arrange
        when(bookingService.createBooking(any(CreateBookingRequest.class)))
                .thenReturn(bookingResponse);

        // Act
        ResponseEntity<?> response = bookingController.createBooking(validRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookingService, times(1)).createBooking(any(CreateBookingRequest.class));
    }

    @Test
    void testGetBooking_ReturnsBookingDetails() {
        // Arrange
        when(bookingService.getBooking(1L))
                .thenReturn(bookingResponse);

        // Act
        ResponseEntity<?> response = bookingController.getBooking(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookingService, times(1)).getBooking(1L);
    }

    @Test
    void testGetAllBookings_ReturnsListOfBookings() {
        // Arrange
        List<BookingResponse> bookingsList = List.of(bookingResponse);
        when(bookingService.getAllBookings())
                .thenReturn(bookingsList);

        // Act
        ResponseEntity<?> response = bookingController.getAllBookings();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookingService, times(1)).getAllBookings();
    }

    @Test
    void testGetBookingsByEmployee_ReturnsEmployeeBookings() {
        // Arrange
        List<BookingResponse> employeeBookings = List.of(bookingResponse);
        when(bookingService.getBookingsByEmployee("EMP_INT_001"))
                .thenReturn(employeeBookings);

        // Act
        ResponseEntity<?> response = bookingController.getBookingsByEmployee("EMP_INT_001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookingService, times(1)).getBookingsByEmployee("EMP_INT_001");
    }
}





