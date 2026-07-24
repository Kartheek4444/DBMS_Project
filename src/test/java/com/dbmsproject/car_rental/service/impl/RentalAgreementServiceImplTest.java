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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalAgreementServiceImplTest {

    @Mock
    private RentalAgreementRepository agreementRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private RentalAgreementMapper rentalAgreementMapper;

    @InjectMocks
    private RentalAgreementServiceImpl agreementService;

    private Booking booking;
    private Staff staff;
    private RentalAgreement agreement;
    private RentalAgreementDto agreementDto;

    @BeforeEach
    void setUp() {
        booking = Booking.builder().bookingId(10L).build();
        staff = Staff.builder().staffId(5L).build();

        agreement = RentalAgreement.builder()
                .agreementId(100L)
                .booking(booking)
                .handledBy(staff)
                .pickupCondition("Good")
                .amount(new BigDecimal("300.00"))
                .pickupTime(LocalDateTime.now())
                .status(AgreementStatus.PENDING)
                .build();

        agreementDto = RentalAgreementDto.builder()
                .agreementId(100L)
                .bookingId(10L)
                .handledByStaffId(5L)
                .pickupCondition("Good")
                .amount(new BigDecimal("300.00"))
                .status(AgreementStatus.PENDING)
                .build();
    }

    @Test
    void createRentalAgreement_Success() {
        when(rentalAgreementMapper.toEntity(agreementDto)).thenReturn(agreement);
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(staffRepository.findById(5L)).thenReturn(Optional.of(staff));
        when(agreementRepository.save(any(RentalAgreement.class))).thenReturn(agreement);
        when(rentalAgreementMapper.toDto(agreement)).thenReturn(agreementDto);

        RentalAgreementDto created = agreementService.createRentalAgreement(agreementDto);

        assertNotNull(created);
        assertEquals(100L, created.getAgreementId());
        verify(agreementRepository).save(any(RentalAgreement.class));
    }

    @Test
    void getRentalAgreementById_Success() {
        when(agreementRepository.findById(100L)).thenReturn(Optional.of(agreement));
        when(rentalAgreementMapper.toDto(agreement)).thenReturn(agreementDto);

        RentalAgreementDto found = agreementService.getRentalAgreementById(100L);

        assertNotNull(found);
        assertEquals(100L, found.getAgreementId());
    }

    @Test
    void getRentalAgreementById_NotFound_ThrowsException() {
        when(agreementRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                agreementService.getRentalAgreementById(999L)
        );
    }

    @Test
    void getAllRentalAgreements_Success() {
        when(agreementRepository.findAll()).thenReturn(List.of(agreement));
        when(rentalAgreementMapper.toDto(agreement)).thenReturn(agreementDto);

        List<RentalAgreementDto> list = agreementService.getAllRentalAgreements();

        assertEquals(1, list.size());
    }
}
