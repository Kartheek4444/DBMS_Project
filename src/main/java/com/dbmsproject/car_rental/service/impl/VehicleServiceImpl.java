package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.VehicleMapper;
import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.model.VehicleCategory;
import com.dbmsproject.car_rental.model.VehicleStatus;
import com.dbmsproject.car_rental.repository.VehicleRepository;
import com.dbmsproject.car_rental.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private VehicleRepository vehicleRepository;

    @Override
    public VehicleDto createVehicle(VehicleDto vehicleDto) {
        Vehicle vehicle = VehicleMapper.toVehicle(vehicleDto);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return VehicleMapper.toVehicleDto(savedVehicle);
    }

    @Override
    public VehicleDto getVehicleById(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));
        return VehicleMapper.toVehicleDto(vehicle);
    }

    @Override
    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        return vehicles.stream()
                .map(VehicleMapper::toVehicleDto)
                .collect(Collectors.toList());
    }

    @Override
    public VehicleDto updateVehicle(Long vehicleId, VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));

        vehicle.setMake(vehicleDto.getMake());
        vehicle.setModel(vehicleDto.getModel());
        vehicle.setYear(vehicleDto.getYear());
        vehicle.setColor(vehicleDto.getColor());
        vehicle.setLicensePlate(vehicleDto.getLicensePlate());
        vehicle.setCategory(vehicleDto.getCategory());
        vehicle.setPricePerDay(vehicleDto.getPricePerDay());
        vehicle.setCurrentMileage(vehicleDto.getCurrentMileage());
        vehicle.setStatus(vehicleDto.getStatus());
        vehicle.setImageUrl(vehicleDto.getImageUrl());
        vehicle.setDescription(vehicleDto.getDescription());

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return VehicleMapper.toVehicleDto(updatedVehicle);
    }

    @Override
    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found with id: " + vehicleId));
        vehicleRepository.delete(vehicle);
    }

    @Override
    public List<VehicleDto> getVehiclesByStatus(VehicleStatus status) {
        return getFilteredVehicles(null, status.name(), null, null);
    }

    @Override
    public List<VehicleDto> getVehiclesByCategory(String category) {
        return getFilteredVehicles(category, null, null, null);
    }

    @Override
    public List<VehicleDto> getVehiclesByPriceRange(Double minPrice, Double maxPrice) {
        return getFilteredVehicles(null, null, minPrice, maxPrice);
    }

    public List<VehicleDto> getFilteredVehicles(String category, String status, Double minPrice, Double maxPrice) {
        VehicleCategory catEnum = (category == null || category.isBlank()) ? null : VehicleCategory.valueOf(category.toUpperCase());
        VehicleStatus statusEnum = (status == null || status.isBlank()) ? null : VehicleStatus.valueOf(status.toUpperCase());
        BigDecimal min = (minPrice == null) ? null : BigDecimal.valueOf(minPrice);
        BigDecimal max = (maxPrice == null) ? null : BigDecimal.valueOf(maxPrice);

        List<Vehicle> vehicles = vehicleRepository.findByFilters(catEnum, statusEnum, min, max);
        return vehicles.stream().map(VehicleMapper::toVehicleDto).collect(Collectors.toList());
    }
}