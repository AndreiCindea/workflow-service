package com.techquarter.workflow.service;

import com.techquarter.workflow.domain.model.Booking;
import com.techquarter.workflow.domain.model.BookingStatus;
import com.techquarter.workflow.domain.model.Employee;
import com.techquarter.workflow.domain.repository.BookingRepository;
import com.techquarter.workflow.dto.BookingResponse;
import com.techquarter.workflow.dto.CreateBookingRequest;
import com.techquarter.workflow.exception.ResourceNotFoundException;
import com.techquarter.workflow.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EmployeeService employeeService;
    private final BookingMapper bookingMapper;

    public BookingResponse createBooking(CreateBookingRequest request) {
        // Validare: returnDate > departureDate
        if (request.getReturnDate().isBefore(request.getDepartureDate())) {
            throw new IllegalArgumentException("Return date must be after departure date");
        }

        // Validare: travelerCount > 0
        if (request.getTravelerCount() <= 0) {
            throw new IllegalArgumentException("Traveler count must be greater than 0");
        }

        // Verifică că employee există
        Employee employee = employeeService.getEmployeeEntity(request.getEmployeeCode());

        // Creează booking
        Booking booking = Booking.builder()
                .employee(employee)
                .resourceType(request.getResourceType())
                .destination(request.getDestination())
                .departureDate(request.getDepartureDate())
                .returnDate(request.getReturnDate())
                .travelerCount(request.getTravelerCount())
                .costCenterRef(request.getCostCenterRef())
                .tripPurpose(request.getTripPurpose())
                .status(BookingStatus.CREATED)
                .build();

        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }

    public BookingResponse getBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking with id " + bookingId + " not found"));
        return bookingMapper.toResponse(booking);
    }

    public List<BookingResponse> getBookingsByEmployee(String employeeCode) {
        Employee employee = employeeService.getEmployeeEntity(employeeCode);
        return bookingRepository.findByEmployeeId(employee.getId())
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }
}
