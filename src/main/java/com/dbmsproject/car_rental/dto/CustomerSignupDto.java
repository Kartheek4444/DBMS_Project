package com.dbmsproject.car_rental.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerSignupDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;
}
