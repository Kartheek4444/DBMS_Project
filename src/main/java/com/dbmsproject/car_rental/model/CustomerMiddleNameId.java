package com.dbmsproject.car_rental.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CustomerMiddleNameId implements Serializable {
    private Long customerId;
    private String middleName;
}
