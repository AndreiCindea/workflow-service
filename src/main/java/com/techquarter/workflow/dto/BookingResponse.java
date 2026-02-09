package com.techquarter.workflow.dto;

import com.techquarter.workflow.domain.model.BookingStatus;
import com.techquarter.workflow.domain.model.ResourceType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {

    private Long id;
    private String employeeCode;
    private String employeeName;
    private ResourceType resourceType;
    private String destination;
    private LocalDateTime departureDate;
    private LocalDateTime returnDate;
    private Integer travelerCount;
    private String costCenterRef;
    private String tripPurpose;
    private BookingStatus status;
}
