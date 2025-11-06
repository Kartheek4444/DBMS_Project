package com.dbmsproject.car_rental.dto;


import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerDto {
    private Long customerId;
    private String avatarUrl;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    private String licenseNo;
    private LocalDate expiryDate;
    private String email;
    private String phoneNumber;
    private String dno;
    private String street;
    private String city;
    private String state;
    private String pinCode;
    private LocalDate registrationDate;
    private List<String> middleNames;
}