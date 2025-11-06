package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.dto.RentalAgreementDto;
import com.dbmsproject.car_rental.dto.StaffDto;
import com.dbmsproject.car_rental.model.AgreementStatus;
import com.dbmsproject.car_rental.service.BookingService;
import com.dbmsproject.car_rental.service.RentalAgreementService;
import com.dbmsproject.car_rental.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RentalAgreementController {

    private final RentalAgreementService rentalAgreementService;
    private final BookingService bookingService;
    private final StaffService staffService;

    // Web pages endpoints
    @GetMapping("/rental-agreements")
    public String showRentalAgreements(Model model) {
        List<RentalAgreementDto> agreements = rentalAgreementService.getAllRentalAgreements();
        model.addAttribute("agreements", agreements);
        return "rental_agreement";
    }

    @GetMapping("/rental-agreements/filter")
    public String filterRentalAgreements(@RequestParam(required = false) String status, Model model) {
        List<RentalAgreementDto> agreements;
        if (status != null && !status.isEmpty()) {
            agreements = rentalAgreementService.getRentalAgreementsByStatus(AgreementStatus.valueOf(status));
        } else {
            agreements = rentalAgreementService.getAllRentalAgreements();
        }
        model.addAttribute("agreements", agreements);
        return "rental_agreement";
    }

    @GetMapping("/rental-agreements/{id}/edit")
    public String editRentalAgreementForm(@PathVariable Long id, Model model) {
        RentalAgreementDto agreement = rentalAgreementService.getRentalAgreementById(id);
        List<StaffDto> staffList = staffService.getAllStaff();

        model.addAttribute("agreement", agreement);
        model.addAttribute("staffList", staffList != null ? staffList : Collections.emptyList());

        // Format dates for HTML5 datetime-local input
        if (agreement.getPickupTime() != null) {
            model.addAttribute("pickupTimeFormatted", agreement.getPickupTime().toString().substring(0, 16));
        }
        if (agreement.getReturnTime() != null) {
            model.addAttribute("returnTimeFormatted", agreement.getReturnTime().toString().substring(0, 16));
        }

        return "rental_agreement_edit";
    }



    @PostMapping("/rental-agreements/{id}/update")
    public String updateRentalAgreementForm(@PathVariable Long id, @ModelAttribute RentalAgreementDto agreementDto) {
        rentalAgreementService.updateRentalAgreement(id, agreementDto);
        return "redirect:/rental-agreements";
    }

    @GetMapping("/rental-agreements/new")
    public String newRentalAgreementForm(Model model) {
        try {
            RentalAgreementDto agreement = new RentalAgreementDto();
            agreement.setStatus(AgreementStatus.PENDING);

            List<BookingDto> bookings = bookingService.getAllBookings();
            List<StaffDto> staffList = staffService.getAllStaff();

            model.addAttribute("agreement", agreement);
            model.addAttribute("bookings", bookings != null ? bookings : Collections.emptyList());
            model.addAttribute("staffList", staffList != null ? staffList : Collections.emptyList());

            return "new_rental_agreement";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load form: " + e.getMessage());
            return "redirect:/rental-agreements";
        }
    }


    @PostMapping("/rental-agreements/create")
    public String createRentalAgreement(@ModelAttribute RentalAgreementDto agreementDto, Model model) {
        try {
            if (agreementDto.getBookingId() == null) {
                throw new IllegalArgumentException("Booking selection is required");
            }

            if (agreementDto.getPickupTime() == null) {
                agreementDto.setPickupTime(LocalDateTime.now());
            }

            if (agreementDto.getStatus() == null) {
                agreementDto.setStatus(AgreementStatus.PENDING);
            }

            rentalAgreementService.createRentalAgreement(agreementDto);
            return "redirect:/rental-agreements";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create rental agreement: " + e.getMessage());
            model.addAttribute("agreement", agreementDto);
            model.addAttribute("bookings", bookingService.getAllBookings());
            model.addAttribute("staffList", staffService.getAllStaff());
            return "new_rental_agreement";
        }
    }



    // API endpoints

    @GetMapping("/api/rental-agreements/{agreementId}")
    public ResponseEntity<RentalAgreementDto> getRentalAgreementById(@PathVariable Long agreementId) {
        RentalAgreementDto rentalAgreement = rentalAgreementService.getRentalAgreementById(agreementId);
        return ResponseEntity.ok(rentalAgreement);
    }

    @PutMapping("/api/rental-agreements/{agreementId}")
    public ResponseEntity<RentalAgreementDto> updateRentalAgreement(
            @PathVariable Long agreementId,
            @RequestBody RentalAgreementDto rentalAgreementDto) {
        RentalAgreementDto updatedAgreement = rentalAgreementService.updateRentalAgreement(agreementId, rentalAgreementDto);
        return ResponseEntity.ok(updatedAgreement);
    }

    @DeleteMapping("/api/rental-agreements/{agreementId}")
    public ResponseEntity<Void> deleteRentalAgreement(@PathVariable Long agreementId) {
        rentalAgreementService.deleteRentalAgreement(agreementId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/rental-agreements")
    public ResponseEntity<List<RentalAgreementDto>> getAllRentalAgreements() {
        List<RentalAgreementDto> rentalAgreements = rentalAgreementService.getAllRentalAgreements();
        return ResponseEntity.ok(rentalAgreements);
    }

    @GetMapping("/api/rental-agreements/booking/{bookingId}")
    public ResponseEntity<List<RentalAgreementDto>> getRentalAgreementsByBookingId(@PathVariable Long bookingId) {
        List<RentalAgreementDto> rentalAgreements = rentalAgreementService.getRentalAgreementsByBookingId(bookingId);
        return ResponseEntity.ok(rentalAgreements);
    }

    @GetMapping("/api/rental-agreements/staff/{staffId}")
    public ResponseEntity<List<RentalAgreementDto>> getRentalAgreementsByStaffId(@PathVariable Long staffId) {
        List<RentalAgreementDto> rentalAgreements = rentalAgreementService.getRentalAgreementsByStaffId(staffId);
        return ResponseEntity.ok(rentalAgreements);
    }

    @GetMapping("/api/rental-agreements/status/{status}")
    public ResponseEntity<List<RentalAgreementDto>> getRentalAgreementsByStatus(@PathVariable AgreementStatus status) {
        List<RentalAgreementDto> rentalAgreements = rentalAgreementService.getRentalAgreementsByStatus(status);
        return ResponseEntity.ok(rentalAgreements);
    }

    @GetMapping("/api/rental-agreements/filter")
    public ResponseEntity<List<RentalAgreementDto>> filterRentalAgreementsApi(
            @RequestParam(required = false) Long bookingId,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupEndDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime returnStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime returnEndDate,
            @RequestParam(required = false) Boolean isCompleted) {
        List<RentalAgreementDto> agreements = rentalAgreementService.filterRentalAgreements(
                bookingId, staffId, pickupStartDate, pickupEndDate, returnStartDate, returnEndDate, isCompleted);
        return ResponseEntity.ok(agreements);
    }

    @PostMapping("/api/rental-agreements/pickup/{bookingId}")
    public ResponseEntity<RentalAgreementDto> recordPickup(
            @PathVariable Long bookingId,
            @RequestBody RentalAgreementDto pickupDetails) {
        RentalAgreementDto agreement = rentalAgreementService.recordPickup(bookingId, pickupDetails);
        return new ResponseEntity<>(agreement, HttpStatus.CREATED);
    }

    @PutMapping("/api/rental-agreements/return/{agreementId}")
    public ResponseEntity<RentalAgreementDto> recordReturn(
            @PathVariable Long agreementId,
            @RequestBody RentalAgreementDto returnDetails) {
        RentalAgreementDto agreement = rentalAgreementService.recordReturn(agreementId, returnDetails);
        return ResponseEntity.ok(agreement);
    }
}