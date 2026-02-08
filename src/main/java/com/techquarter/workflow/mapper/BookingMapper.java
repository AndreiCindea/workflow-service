package com.techquarter.workflow.mapper;

import com.techquarter.workflow.domain.model.Booking;
import com.techquarter.workflow.dto.BookingResponse;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .employeeCode(booking.getEmployee().getEmployeeCode())
                .employeeName(booking.getEmployee().getName())
                .resourceType(booking.getResourceType())
                .destination(booking.getDestination())
                .departureDate(booking.getDepartureDate())
                .returnDate(booking.getReturnDate())
                .status(booking.getStatus())
                .build();
    }
}
