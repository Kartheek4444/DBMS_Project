package com.dbmsproject.car_rental.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rental_agreements")
public class RentalAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agreement_id")
    private Long agreementId;

    @OneToOne(optional = false)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "pickup_mileage")
    private Integer pickupMileage;

    @Column(name = "return_mileage")
    private Integer returnMileage;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "pickup_condition", length = 1000)
    private String pickupCondition;

    @Column(name = "return_condition", length = 1000)
    private String returnCondition;

    @ManyToOne
    @JoinColumn(name = "handled_by")
    private Staff handledBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private AgreementStatus status;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
}
