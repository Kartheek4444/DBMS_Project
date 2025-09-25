package com.dbmsproject.car_rental.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffId;

    private String firstName;
    private String lastName;

    private Boolean isActive = true;
    private String email;
    private String phone;
    private String position;
    private LocalDate hireDate;

    @ManyToOne
    @JoinColumn(
        name = "managed_id",
        foreignKey = @ForeignKey(
            name = "fk_staff_manager",
            foreignKeyDefinition = "FOREIGN KEY (managed_id) REFERENCES staff(staff_id) ON DELETE SET NULL"
        )
    )
    private Staff manager;

    @OneToMany(mappedBy = "handledBy")
    private List<Maintenance> maintenances = new ArrayList<>();

    @OneToMany(mappedBy = "handledBy")
    private List<RentalAgreement> rentalAgreements = new ArrayList<>();

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StaffMiddleName> middleNames = new ArrayList<>();
}
