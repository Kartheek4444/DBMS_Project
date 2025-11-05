package com.dbmsproject.car_rental.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDto {
    private Long staffId;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String email;
    private String phone;
    private String password;
    private String position;
    private LocalDate hireDate;
    private Long managerId;
    private List<String> middleNames;
}
