package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.RentalAgreementDto;
import com.dbmsproject.car_rental.model.AgreementStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalAgreementService {
    RentalAgreementDto createRentalAgreement(RentalAgreementDto rentalAgreementDto);
    RentalAgreementDto getRentalAgreementById(Long agreementId);
    RentalAgreementDto updateRentalAgreement(Long agreementId, RentalAgreementDto rentalAgreementDto);
    void deleteRentalAgreement(Long agreementId);
    List<RentalAgreementDto> getAllRentalAgreements();
    List<RentalAgreementDto> getRentalAgreementsByBookingId(Long bookingId);
    List<RentalAgreementDto> getRentalAgreementsByStaffId(Long staffId);
    List<RentalAgreementDto> getRentalAgreementsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<RentalAgreementDto> getActiveRentalAgreements();
    List<RentalAgreementDto> getCompletedRentalAgreements();
    List<RentalAgreementDto> getRentalAgreementsByStatus(AgreementStatus status);
    RentalAgreementDto recordPickup(Long bookingId, RentalAgreementDto pickupDetails);
    RentalAgreementDto recordReturn(Long agreementId, RentalAgreementDto returnDetails);
    RentalAgreementDto getRentalAgreementByBookingId(Long bookingId);

    List<RentalAgreementDto> filterRentalAgreements(
            Long bookingId,
            Long staffId,
            LocalDateTime pickupStartDate,
            LocalDateTime pickupEndDate,
            LocalDateTime returnStartDate,
            LocalDateTime returnEndDate,
            Boolean isCompleted
    );
}
