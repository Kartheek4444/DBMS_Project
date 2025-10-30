package com.dbmsproject.car_rental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDto {

    private Long maintenanceId;

    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;

    private Long staffId;

    @NotNull(message = "Service date is required")
    private LocalDateTime serviceDate;

    @NotNull(message = "Service type is required")
    @Size(min = 2, max = 100, message = "Service type must be between 2 and 100 characters")
    private String serviceType;

    @Positive(message = "Cost must be positive")
    private Double cost;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    private LocalDateTime nextServiceDate;

    @Positive(message = "Next service mileage must be positive")
    private Integer nextServiceMileage;
}
