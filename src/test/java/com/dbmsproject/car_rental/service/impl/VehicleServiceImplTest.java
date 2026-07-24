package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.model.VehicleCategory;
import com.dbmsproject.car_rental.model.VehicleStatus;
import com.dbmsproject.car_rental.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle;
    private VehicleDto vehicleDto;

    @BeforeEach
    void setUp() {
        vehicle = Vehicle.builder()
                .vehicleId(1L)
                .make("Honda")
                .model("Civic")
                .year(2023)
                .licensePlate("ABC1234")
                .category(VehicleCategory.CAR)
                .pricePerDay(new BigDecimal("60.00"))
                .status(VehicleStatus.AVAILABLE)
                .build();

        vehicleDto = VehicleDto.builder()
                .make("Honda")
                .model("Civic")
                .year(2023)
                .licensePlate("ABC1234")
                .category(VehicleCategory.CAR)
                .pricePerDay(new BigDecimal("60.00"))
                .status(VehicleStatus.AVAILABLE)
                .build();
    }

    @Test
    void createVehicle_Success() {
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleDto created = vehicleService.createVehicle(vehicleDto);

        assertNotNull(created);
        assertEquals(1L, created.getVehicleId());
        assertEquals("Honda", created.getMake());
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void getVehicleById_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        VehicleDto found = vehicleService.getVehicleById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getVehicleId());
    }

    @Test
    void getVehicleById_NotFound_ThrowsException() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                vehicleService.getVehicleById(99L)
        );
    }

    @Test
    void getAllVehicles_Success() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        List<VehicleDto> vehicles = vehicleService.getAllVehicles();

        assertEquals(1, vehicles.size());
    }

    @Test
    void getVehiclesByStatus_Success() {
        when(vehicleRepository.findByFilters(eq(null), eq(VehicleStatus.AVAILABLE), eq(null), eq(null))).thenReturn(List.of(vehicle));

        List<VehicleDto> available = vehicleService.getVehiclesByStatus(VehicleStatus.AVAILABLE);

        assertEquals(1, available.size());
    }

    @Test
    void updateVehicle_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        vehicleDto.setPricePerDay(new BigDecimal("70.00"));
        VehicleDto updated = vehicleService.updateVehicle(1L, vehicleDto);

        assertNotNull(updated);
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void deleteVehicle_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository).delete(vehicle);
    }
}
