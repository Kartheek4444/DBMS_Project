package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.PaymentDto;
import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.Payment;

public class PaymentMapper {

    public static PaymentDto toDto(Payment payment) {
        if (payment == null) return null;
        return PaymentDto.builder()
                .paymentId(payment.getPaymentId())
                .bookingId(payment.getBooking() != null ? payment.getBooking().getBookingId() : null)
                .paymentType(payment.getPaymentType())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .transactionDate(payment.getTransactionDate())
                .status(payment.getStatus())
                .build();
    }

    public static Payment toEntity(PaymentDto dto, Booking booking) {
        if (dto == null) return null;
        return Payment.builder()
                .paymentId(dto.getPaymentId())
                .booking(booking)
                .paymentType(dto.getPaymentType())
                .amount(dto.getAmount())
                .paymentMethod(dto.getPaymentMethod())
                .transactionDate(dto.getTransactionDate())
                .status(dto.getStatus())
                .build();
    }
}
