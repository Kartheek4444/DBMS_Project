package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.PaymentDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.Payment;
import com.dbmsproject.car_rental.model.PaymentMethod;
import com.dbmsproject.car_rental.model.PaymentStatus;
import com.dbmsproject.car_rental.model.PaymentType;

import com.dbmsproject.car_rental.repository.BookingRepository;
import com.dbmsproject.car_rental.repository.PaymentRepository;
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
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Booking booking;
    private Payment payment;
    private PaymentDto paymentDto;

    @BeforeEach
    void setUp() {
        booking = Booking.builder()
                .bookingId(50L)
                .build();

        payment = Payment.builder()
                .paymentId(1L)
                .booking(booking)
                .amount(new BigDecimal("250.00"))
                .paymentType(PaymentType.RENTAL_FEE)
                .paymentMethod(PaymentMethod.CARD)
                .transactionDate(LocalDateTime.now())
                .status(PaymentStatus.COMPLETED)
                .build();

        paymentDto = PaymentDto.builder()
                .bookingId(50L)
                .amount(new BigDecimal("250.00"))
                .paymentType(PaymentType.RENTAL_FEE)
                .paymentMethod(PaymentMethod.CARD)
                .status(PaymentStatus.COMPLETED)
                .build();
    }

    @Test
    void createPayment_Success() {
        when(bookingRepository.findById(50L)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDto result = paymentService.createPayment(paymentDto);

        assertNotNull(result);
        assertEquals(1L, result.getPaymentId());
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void createPayment_BookingNotFound_ThrowsException() {
        when(bookingRepository.findById(99L)).thenReturn(Optional.empty());
        paymentDto.setBookingId(99L);

        assertThrows(ResourceNotFoundException.class, () ->
                paymentService.createPayment(paymentDto)
        );
    }

    @Test
    void getPaymentById_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentDto result = paymentService.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getPaymentId());
    }

    @Test
    void getPaymentByBookingId_Success() {
        when(paymentRepository.findByBooking_BookingId(50L)).thenReturn(Optional.of(payment));

        PaymentDto result = paymentService.getPaymentByBookingId(50L);

        assertNotNull(result);
        assertEquals(50L, result.getBookingId());
    }

    @Test
    void getAllPayments_Success() {
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        List<PaymentDto> payments = paymentService.getAllPayments();

        assertEquals(1, payments.size());
    }

    @Test
    void updatePaymentStatus_Success() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentDto updated = paymentService.updatePaymentStatus(1L, PaymentStatus.REFUNDED);

        assertNotNull(updated);
        verify(paymentRepository).save(payment);
        assertEquals(PaymentStatus.REFUNDED, payment.getStatus());
    }
}
