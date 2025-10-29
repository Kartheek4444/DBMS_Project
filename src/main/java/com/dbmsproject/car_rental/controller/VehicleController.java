package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/vehicles")
@AllArgsConstructor
public class VehicleController {
    private VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle(@RequestBody VehicleDto vehicleDto) {
        VehicleDto savedVehicle = vehicleService.createVehicle(vehicleDto);
        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable("id") Long vehicleId) {
        VehicleDto vehicleDto = vehicleService.getVehicleById(vehicleId);
        return ResponseEntity.ok(vehicleDto);
    }

    @GetMapping
    public ResponseEntity<List<VehicleDto>> getAllVehicles() {
        List<VehicleDto> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDto> updateVehicle(@PathVariable("id") Long vehicleId, @RequestBody VehicleDto vehicleDto) {
        VehicleDto updatedVehicle = vehicleService.updateVehicle(vehicleId, vehicleDto);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}
