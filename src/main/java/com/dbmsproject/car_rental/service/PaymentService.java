package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.PaymentDto;
import com.dbmsproject.car_rental.model.PaymentStatus;

import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    PaymentDto getPaymentById(Long paymentId);
    PaymentDto getPaymentByBookingId(Long bookingId);
    List<PaymentDto> getAllPayments();
    PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus status);
}
