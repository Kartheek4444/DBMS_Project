package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.model.Vehicle;

import java.util.ArrayList;

public class VehicleMapper {
    public static VehicleDto toVehicleDto(Vehicle vehicle) {
        return VehicleDto.builder()
                .vehicleId(vehicle.getVehicleId())
                .make(vehicle.getMake())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .color(vehicle.getColor())
                .licensePlate(vehicle.getLicensePlate())
                .category(vehicle.getCategory())
                .pricePerDay(vehicle.getPricePerDay())
                .currentMileage(vehicle.getCurrentMileage())
                .status(vehicle.getStatus())
                .imageUrl(vehicle.getImageUrl())
                .description(vehicle.getDescription())  // Add this line
                .build();
    }

    public static Vehicle toVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = Vehicle.builder()
                .vehicleId(vehicleDto.getVehicleId())
                .make(vehicleDto.getMake())
                .model(vehicleDto.getModel())
                .year(vehicleDto.getYear())
                .color(vehicleDto.getColor())
                .licensePlate(vehicleDto.getLicensePlate())
                .category(vehicleDto.getCategory())
                .pricePerDay(vehicleDto.getPricePerDay())
                .currentMileage(vehicleDto.getCurrentMileage())
                .status(vehicleDto.getStatus())
                .imageUrl(vehicleDto.getImageUrl())
                .description(vehicleDto.getDescription())  // Add this line
                .build();

        if (vehicle.getBookings() == null) vehicle.setBookings(new ArrayList<>());
        if (vehicle.getMaintenances() == null) vehicle.setMaintenances(new ArrayList<>());

        return vehicle;
    }

}
