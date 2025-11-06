package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.RentalAgreementDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.RentalAgreementMapper;
import com.dbmsproject.car_rental.model.AgreementStatus;
import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.RentalAgreement;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.repository.BookingRepository;
import com.dbmsproject.car_rental.repository.RentalAgreementRepository;
import com.dbmsproject.car_rental.repository.StaffRepository;
import com.dbmsproject.car_rental.service.RentalAgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalAgreementServiceImpl implements RentalAgreementService {

    private final RentalAgreementRepository rentalAgreementRepository;
    private final BookingRepository bookingRepository;
    private final StaffRepository staffRepository;
    private final RentalAgreementMapper rentalAgreementMapper;

    @Override
    public RentalAgreementDto createRentalAgreement(RentalAgreementDto rentalAgreementDto) {
        RentalAgreement rentalAgreement = rentalAgreementMapper.toEntity(rentalAgreementDto);

        if (rentalAgreementDto.getBookingId() != null) {
            Booking booking = bookingRepository.findById(rentalAgreementDto.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + rentalAgreementDto.getBookingId()));
            rentalAgreement.setBooking(booking);
        }

        if (rentalAgreementDto.getHandledByStaffId() != null) {
            Staff staff = staffRepository.findById(rentalAgreementDto.getHandledByStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + rentalAgreementDto.getHandledByStaffId()));
            rentalAgreement.setHandledBy(staff);
        }

        RentalAgreement savedAgreement = rentalAgreementRepository.save(rentalAgreement);
        return rentalAgreementMapper.toDto(savedAgreement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> getRentalAgreementsByStatus(AgreementStatus status) {
        return rentalAgreementRepository.findByStatus(status).stream()
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RentalAgreementDto getRentalAgreementById(Long agreementId) {
        RentalAgreement rentalAgreement = rentalAgreementRepository.findById(agreementId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental Agreement not found with id: " + agreementId));
        return rentalAgreementMapper.toDto(rentalAgreement);
    }

    @Override
    public RentalAgreementDto updateRentalAgreement(Long agreementId, RentalAgreementDto rentalAgreementDto) {
        RentalAgreement existingAgreement = rentalAgreementRepository.findById(agreementId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental Agreement not found with id: " + agreementId));

        existingAgreement.setPickupMileage(rentalAgreementDto.getPickupMileage());
        existingAgreement.setReturnMileage(rentalAgreementDto.getReturnMileage());
        existingAgreement.setPickupTime(rentalAgreementDto.getPickupTime());
        existingAgreement.setReturnTime(rentalAgreementDto.getReturnTime());
        existingAgreement.setPickupCondition(rentalAgreementDto.getPickupCondition());
        existingAgreement.setReturnCondition(rentalAgreementDto.getReturnCondition());
        existingAgreement.setStatus(rentalAgreementDto.getStatus()); // Add this line
        existingAgreement.setAmount(rentalAgreementDto.getAmount()); // Add this line

        if (rentalAgreementDto.getBookingId() != null && !rentalAgreementDto.getBookingId().equals(
                existingAgreement.getBooking() != null ? existingAgreement.getBooking().getBookingId() : null)) {
            Booking booking = bookingRepository.findById(rentalAgreementDto.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + rentalAgreementDto.getBookingId()));
            existingAgreement.setBooking(booking);
        }

        if (rentalAgreementDto.getHandledByStaffId() != null) {
            Staff staff = staffRepository.findById(rentalAgreementDto.getHandledByStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + rentalAgreementDto.getHandledByStaffId()));
            existingAgreement.setHandledBy(staff);
        }

        RentalAgreement updatedAgreement = rentalAgreementRepository.save(existingAgreement);
        return rentalAgreementMapper.toDto(updatedAgreement);
    }


    @Override
    public void deleteRentalAgreement(Long agreementId) {
        if (!rentalAgreementRepository.existsById(agreementId)) {
            throw new ResourceNotFoundException("Rental Agreement not found with id: " + agreementId);
        }
        rentalAgreementRepository.deleteById(agreementId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> getAllRentalAgreements() {
        return rentalAgreementRepository.findAll().stream()
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> getRentalAgreementsByBookingId(Long bookingId) {
        return rentalAgreementRepository.findByBooking_BookingId(bookingId).stream()
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> getRentalAgreementsByStaffId(Long staffId) {
        return rentalAgreementRepository.findByHandledBy_StaffId(staffId).stream()
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> getRentalAgreementsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return rentalAgreementRepository.findByPickupTimeBetween(startDate, endDate).stream()
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> getActiveRentalAgreements() {
        return rentalAgreementRepository.findByReturnTimeIsNull().stream()
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> getCompletedRentalAgreements() {
        return rentalAgreementRepository.findByReturnTimeIsNotNull().stream()
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RentalAgreementDto recordPickup(Long bookingId, RentalAgreementDto pickupDetails) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        RentalAgreement rentalAgreement = RentalAgreement.builder()
                .booking(booking)
                .pickupMileage(pickupDetails.getPickupMileage())
                .pickupTime(pickupDetails.getPickupTime() != null ? pickupDetails.getPickupTime() : LocalDateTime.now())
                .pickupCondition(pickupDetails.getPickupCondition())
                .build();

        if (pickupDetails.getHandledByStaffId() != null) {
            Staff staff = staffRepository.findById(pickupDetails.getHandledByStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + pickupDetails.getHandledByStaffId()));
            rentalAgreement.setHandledBy(staff);
        }

        RentalAgreement savedAgreement = rentalAgreementRepository.save(rentalAgreement);
        return rentalAgreementMapper.toDto(savedAgreement);
    }

    @Override
    public RentalAgreementDto recordReturn(Long agreementId, RentalAgreementDto returnDetails) {
        RentalAgreement rentalAgreement = rentalAgreementRepository.findById(agreementId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental Agreement not found with id: " + agreementId));

        rentalAgreement.setReturnMileage(returnDetails.getReturnMileage());
        rentalAgreement.setReturnTime(returnDetails.getReturnTime() != null ? returnDetails.getReturnTime() : LocalDateTime.now());
        rentalAgreement.setReturnCondition(returnDetails.getReturnCondition());

        if (returnDetails.getHandledByStaffId() != null) {
            Staff staff = staffRepository.findById(returnDetails.getHandledByStaffId())
                    .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + returnDetails.getHandledByStaffId()));
            rentalAgreement.setHandledBy(staff);
        }

        RentalAgreement updatedAgreement = rentalAgreementRepository.save(rentalAgreement);
        return rentalAgreementMapper.toDto(updatedAgreement);
    }

    @Override
    @Transactional(readOnly = true)
    public RentalAgreementDto getRentalAgreementByBookingId(Long bookingId) {
        RentalAgreement rentalAgreement = rentalAgreementRepository.findByBooking_BookingId(bookingId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Rental Agreement not found for booking id: " + bookingId));
        return rentalAgreementMapper.toDto(rentalAgreement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalAgreementDto> filterRentalAgreements(Long bookingId, Long staffId,
                                                           LocalDateTime pickupStartDate, LocalDateTime pickupEndDate,
                                                           LocalDateTime returnStartDate, LocalDateTime returnEndDate,
                                                           Boolean isCompleted) {
        return rentalAgreementRepository.findAll().stream()
                .filter(agreement -> bookingId == null || (agreement.getBooking() != null && agreement.getBooking().getBookingId().equals(bookingId)))
                .filter(agreement -> staffId == null || (agreement.getHandledBy() != null && agreement.getHandledBy().getStaffId().equals(staffId)))
                .filter(agreement -> pickupStartDate == null || (agreement.getPickupTime() != null && !agreement.getPickupTime().isBefore(pickupStartDate)))
                .filter(agreement -> pickupEndDate == null || (agreement.getPickupTime() != null && !agreement.getPickupTime().isAfter(pickupEndDate)))
                .filter(agreement -> returnStartDate == null || (agreement.getReturnTime() != null && !agreement.getReturnTime().isBefore(returnStartDate)))
                .filter(agreement -> returnEndDate == null || (agreement.getReturnTime() != null && !agreement.getReturnTime().isAfter(returnEndDate)))
                .filter(agreement -> isCompleted == null || (isCompleted ? agreement.getReturnTime() != null : agreement.getReturnTime() == null))
                .map(rentalAgreementMapper::toDto)
                .collect(Collectors.toList());
    }
}
