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
public class StaffMiddleNameId implements Serializable {
    private Long staffId;
    private String middleName;
}
