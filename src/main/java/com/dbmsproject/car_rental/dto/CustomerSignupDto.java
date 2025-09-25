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

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Phone number is invalid")
    private String phoneNumber;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
