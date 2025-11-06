package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.StaffDto;

import java.util.List;

public interface StaffService {
    StaffDto createStaff(StaffDto staffDto);
    StaffDto getStaffById(Long staffId);
    StaffDto updateStaff(Long staffId, StaffDto staffDto);
    boolean validateStaff(String email, String password);
    void deleteStaff(Long staffId);
    List<StaffDto> getAllStaff();
    StaffDto getStaffByEmail(String email);
}