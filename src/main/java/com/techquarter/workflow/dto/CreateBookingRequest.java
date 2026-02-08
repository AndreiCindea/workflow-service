package com.techquarter.workflow.dto;

import com.techquarter.workflow.domain.model.ResourceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateBookingRequest {

    @NotNull
    private String employeeCode;

    @NotNull
    private ResourceType resourceType;

    private String destination;

    @NotNull
    private LocalDateTime departureDate;

    @NotNull
    private LocalDateTime returnDate;

    @Positive
    private int travelerCount;

    private String costCenterRef;

    private String tripPurpose;
}
