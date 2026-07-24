package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.PaymentDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.PaymentMapper;
import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.Payment;
import com.dbmsproject.car_rental.model.PaymentStatus;
import com.dbmsproject.car_rental.repository.BookingRepository;
import com.dbmsproject.car_rental.repository.PaymentRepository;
import com.dbmsproject.car_rental.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Booking booking = bookingRepository.findById(paymentDto.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + paymentDto.getBookingId()));

        Payment payment = PaymentMapper.toEntity(paymentDto, booking);
        if (payment.getStatus() == null) {
            payment.setStatus(PaymentStatus.COMPLETED);
        }
        Payment savedPayment = paymentRepository.save(payment);
        return PaymentMapper.toDto(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        return PaymentMapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentDto getPaymentByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBooking_BookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking id: " + bookingId));
        return PaymentMapper.toDto(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + paymentId));
        payment.setStatus(status);
        Payment updated = paymentRepository.save(payment);
        return PaymentMapper.toDto(updated);
    }
}
