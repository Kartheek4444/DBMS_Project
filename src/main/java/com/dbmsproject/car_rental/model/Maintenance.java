// Maintenance.java
package com.dbmsproject.car_rental.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "maintenance")
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintenanceId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff handledBy;

    private LocalDateTime serviceDate;

    private String serviceType;
    private Double cost;

    @Column(length = 1000)
    private String description;

    @Column(name = "next_service_date")
    private LocalDateTime nextServiceDate;

    @Column(name = "next_service_mileage")
    private Integer nextServiceMileage;
}
