package com.dbmsproject.car_rental.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "staff_middle_names")
public class StaffMiddleName {

    @EmbeddedId
    private StaffMiddleNameId id;

    @ManyToOne
    @MapsId("staffId") // maps staffId inside composite key
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;
}
