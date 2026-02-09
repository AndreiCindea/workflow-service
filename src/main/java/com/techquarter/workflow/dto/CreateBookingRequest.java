package com.techquarter.workflow.dto;

import com.techquarter.workflow.domain.model.ResourceType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
