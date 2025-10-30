package com.dbmsproject.car_rental.dto;

import com.dbmsproject.car_rental.model.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {

    private Long bookingId;
    private Long customerId;
    private String customerName;
    private Long vehicleId;
    private String vehicleMake;
    private String vehicleModel;
    private LocalDateTime pickupDate;
    private LocalDateTime returnDate;
    private LocalDateTime bookingDate;
    private BookingStatus status;
    private BigDecimal depositAmount;
    private BigDecimal totalAmount;
}
