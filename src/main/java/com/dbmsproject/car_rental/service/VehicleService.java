package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.model.VehicleStatus;

import java.util.List;

public interface VehicleService {
    VehicleDto createVehicle(VehicleDto vehicleDto);
    VehicleDto getVehicleById(Long vehicleId);
    List<VehicleDto> getAllVehicles();
    VehicleDto updateVehicle(Long vehicleId, VehicleDto vehicleDto);
    void deleteVehicle(Long vehicleId);
    List<VehicleDto> getVehiclesByStatus(VehicleStatus status);
    List<VehicleDto> getVehiclesByCategory(String category);
    List<VehicleDto> getVehiclesByPriceRange(Double minPrice, Double maxPrice);
    List<VehicleDto> getVehiclesByMake(String make);
}