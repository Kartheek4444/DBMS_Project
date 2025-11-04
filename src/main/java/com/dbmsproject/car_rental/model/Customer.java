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
@Entity
@Builder
@ToString
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "license_no", unique = true)
    private String licenseNo;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    private String dno;
    private String street;
    private String city;
    private String state;
    private String pinCode;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerMiddleName> middleNames = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDate.now();
    }

    public String getName() {
        StringBuilder fullName = new StringBuilder(firstName);
            for (CustomerMiddleName middleName : middleNames) {         
                fullName.append(" ").append(middleName.getMiddleName());
        }
        fullName.append(" ").append(lastName);
        return fullName.toString();
    }
}
