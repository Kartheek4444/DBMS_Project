package com.dbmsproject.car_rental.dto;

import com.dbmsproject.car_rental.model.VehicleCategory;
import com.dbmsproject.car_rental.model.VehicleStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleDto {
    private Long vehicleId;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private String licensePlate;
    private VehicleCategory category;
    private BigDecimal pricePerDay;
    private Integer currentMileage;
    private VehicleStatus status;
}
