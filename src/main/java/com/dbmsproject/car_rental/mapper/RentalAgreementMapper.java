package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.RentalAgreementDto;
import com.dbmsproject.car_rental.model.RentalAgreement;
import org.springframework.stereotype.Component;

@Component
public class RentalAgreementMapper {

    public RentalAgreementDto toDto(RentalAgreement rentalAgreement) {
        if (rentalAgreement == null) {
            return null;
        }

        return RentalAgreementDto.builder()
                .agreementId(rentalAgreement.getAgreementId())
                .bookingId(rentalAgreement.getBooking() != null ? rentalAgreement.getBooking().getBookingId() : null)
                .pickupMileage(rentalAgreement.getPickupMileage())
                .returnMileage(rentalAgreement.getReturnMileage())
                .pickupTime(rentalAgreement.getPickupTime())
                .returnTime(rentalAgreement.getReturnTime())
                .pickupCondition(rentalAgreement.getPickupCondition())
                .returnCondition(rentalAgreement.getReturnCondition())
                .handledByStaffId(rentalAgreement.getHandledBy() != null ? rentalAgreement.getHandledBy().getStaffId() : null)
                .handledByStaffName(rentalAgreement.getHandledBy() != null ? rentalAgreement.getHandledBy().getName() : null)
                .status(rentalAgreement.getStatus())
                .amount(rentalAgreement.getAmount())
                .build();
    }

    public RentalAgreement toEntity(RentalAgreementDto dto) {
        if (dto == null) {
            return null;
        }

        return RentalAgreement.builder()
                .agreementId(dto.getAgreementId())
                .pickupMileage(dto.getPickupMileage())
                .returnMileage(dto.getReturnMileage())
                .pickupTime(dto.getPickupTime())
                .returnTime(dto.getReturnTime())
                .pickupCondition(dto.getPickupCondition())
                .returnCondition(dto.getReturnCondition())
                .status(dto.getStatus())
                .amount(dto.getAmount())
                .build();
    }
}