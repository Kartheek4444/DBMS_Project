package com.dbmsproject.car_rental.dto;

import com.dbmsproject.car_rental.model.PaymentMethod;
import com.dbmsproject.car_rental.model.PaymentStatus;
import com.dbmsproject.car_rental.model.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long paymentId;
    private Long bookingId;
    private PaymentType paymentType;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private LocalDateTime transactionDate;
    private PaymentStatus status;
}
