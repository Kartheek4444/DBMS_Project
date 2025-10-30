package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.RentalAgreementDto;
import com.dbmsproject.car_rental.service.RentalAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/rental-agreements")
@RequiredArgsConstructor
public class RentalAgreementController {

    private final RentalAgreementService rentalAgreementService;

    @PostMapping
    public ResponseEntity<RentalAgreementDto> createRentalAgreement(@RequestBody RentalAgreementDto rentalAgreementDto) {
        RentalAgreementDto createdAgreement = rentalAgreementService.createRentalAgreement(rentalAgreementDto);
        return new ResponseEntity<>(createdAgreement, HttpStatus.CREATED);
    }

    @GetMapping("/{agreementId}")
    public ResponseEntity<RentalAgreementDto> getRentalAgreementById(@PathVariable Long agreementId) {
        RentalAgreementDto rentalAgreement = rentalAgreementService.getRentalAgreementById(agreementId);
        return ResponseEntity.ok(rentalAgreement);
    }

    @PutMapping("/{agreementId}")
    public ResponseEntity<RentalAgreementDto> updateRentalAgreement(
            @PathVariable Long agreementId,
            @RequestBody RentalAgreementDto rentalAgreementDto) {
        RentalAgreementDto updatedAgreement = rentalAgreementService.updateRentalAgreement(agreementId, rentalAgreementDto);
        return ResponseEntity.ok(updatedAgreement);
    }

    @DeleteMapping("/{agreementId}")
    public ResponseEntity<Void> deleteRentalAgreement(@PathVariable Long agreementId) {
        rentalAgreementService.deleteRentalAgreement(agreementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<RentalAgreementDto>> getAllRentalAgreements() {
        List<RentalAgreementDto> agreements = rentalAgreementService.getAllRentalAgreements();
        return ResponseEntity.ok(agreements);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<RentalAgreementDto>> getRentalAgreementsByBookingId(@PathVariable Long bookingId) {
        List<RentalAgreementDto> agreements = rentalAgreementService.getRentalAgreementsByBookingId(bookingId);
        return ResponseEntity.ok(agreements);
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<List<RentalAgreementDto>> getRentalAgreementsByStaffId(@PathVariable Long staffId) {
        List<RentalAgreementDto> agreements = rentalAgreementService.getRentalAgreementsByStaffId(staffId);
        return ResponseEntity.ok(agreements);
    }

    @GetMapping("/active")
    public ResponseEntity<List<RentalAgreementDto>> getActiveRentalAgreements() {
        List<RentalAgreementDto> agreements = rentalAgreementService.getActiveRentalAgreements();
        return ResponseEntity.ok(agreements);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<RentalAgreementDto>> getCompletedRentalAgreements() {
        List<RentalAgreementDto> agreements = rentalAgreementService.getCompletedRentalAgreements();
        return ResponseEntity.ok(agreements);
    }

    @PostMapping("/pickup/{bookingId}")
    public ResponseEntity<RentalAgreementDto> recordPickup(
            @PathVariable Long bookingId,
            @RequestBody RentalAgreementDto pickupDetails) {
        RentalAgreementDto agreement = rentalAgreementService.recordPickup(bookingId, pickupDetails);
        return new ResponseEntity<>(agreement, HttpStatus.CREATED);
    }

    @PutMapping("/return/{agreementId}")
    public ResponseEntity<RentalAgreementDto> recordReturn(
            @PathVariable Long agreementId,
            @RequestBody RentalAgreementDto returnDetails) {
        RentalAgreementDto agreement = rentalAgreementService.recordReturn(agreementId, returnDetails);
        return ResponseEntity.ok(agreement);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<RentalAgreementDto>> filterRentalAgreements(
            @RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) LocalDateTime pickupStartDate,
            @RequestParam(required = false) LocalDateTime pickupEndDate,
            @RequestParam(required = false) LocalDateTime returnStartDate,
            @RequestParam(required = false) LocalDateTime returnEndDate,
            @RequestParam(required = false) Boolean isCompleted) {
        List<RentalAgreementDto> agreements = rentalAgreementService.filterRentalAgreements(
                bookingId, staffId, pickupStartDate, pickupEndDate, returnStartDate, returnEndDate, isCompleted);
        return ResponseEntity.ok(agreements);
    }
}
