package com.dbmsproject.car_rental.dto;

import com.dbmsproject.car_rental.model.AgreementStatus;
import lombok.*;

import java.math.BigDecimal;
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
    private AgreementStatus status;
    private BigDecimal amount;
}
