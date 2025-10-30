package com.dbmsproject.car_rental.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalAgreementDto {

    private Long agreementId;
    private Long bookingId;
    private Integer pickupMileage;
    private Integer returnMileage;
    private LocalDateTime pickupTime;
    private LocalDateTime returnTime;
    private String pickupCondition;
    private String returnCondition;
    private Long handledByStaffId;
    private String handledByStaffName;
}
