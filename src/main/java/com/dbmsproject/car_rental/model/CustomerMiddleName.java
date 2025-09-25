package com.dbmsproject.car_rental.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer_middle_names")
public class CustomerMiddleName {

    @EmbeddedId
    private CustomerMiddleNameId id;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
