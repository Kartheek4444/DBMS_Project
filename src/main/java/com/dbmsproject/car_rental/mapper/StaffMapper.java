package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.StaffDto;
import com.dbmsproject.car_rental.model.Roles;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.model.StaffMiddleName;
import com.dbmsproject.car_rental.model.StaffMiddleNameId;

import java.util.*;

public class StaffMapper {
    public static StaffDto toStaffDto(Staff staff) {
        List<String> middleNames = Optional.ofNullable(staff.getMiddleNames())
                .orElse(Collections.emptyList())
                .stream()
                .map(sm -> sm.getId() == null ? null : sm.getId().getMiddleName())
                .filter(Objects::nonNull)
                .toList();

        return StaffDto.builder()
                .staffId(staff.getStaffId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .isActive(staff.getIsActive())
                .email(staff.getEmail())
                .phone(staff.getPhone())
                .password(staff.getPassword())
                .position(staff.getPosition())
                .hireDate(staff.getHireDate())
                .role(staff.getRole().name())
                .managerId(staff.getManager() != null ? staff.getManager().getStaffId() : null)
                .middleNames(middleNames.isEmpty() ? null : middleNames)
                .avatarUrl(staff.getAvatarUrl())
                .build();
    }

    public static Staff toStaff(StaffDto staffDto) {
        Staff staff = Staff.builder()
                .firstName(staffDto.getFirstName())
                .lastName(staffDto.getLastName())
                .isActive(staffDto.getIsActive())
                .email(staffDto.getEmail())
                .phone(staffDto.getPhone())
                .password(staffDto.getPassword())
                .position(staffDto.getPosition())
                .hireDate(staffDto.getHireDate())
                .role(staffDto.getRole() != null ? Roles.valueOf(staffDto.getRole()) : Roles.STAFF)
                .avatarUrl(staffDto.getAvatarUrl())
                .build();

        if (staff.getMaintenances() == null) staff.setMaintenances(new ArrayList<>());
        if (staff.getRentalAgreements() == null) staff.setRentalAgreements(new ArrayList<>());
        if (staff.getMiddleNames() == null) staff.setMiddleNames(new ArrayList<>());

        if (staffDto.getMiddleNames() != null) {
            List<StaffMiddleName> middleNames = staffDto.getMiddleNames().stream()
                    .filter(Objects::nonNull)
                    .map(mn -> {
                        StaffMiddleName sm = new StaffMiddleName();
                        StaffMiddleNameId id = new StaffMiddleNameId();
                        id.setStaffId(staff.getStaffId());
                        id.setMiddleName(mn);
                        sm.setId(id);
                        sm.setStaff(staff);
                        return sm;
                    }).toList();
            staff.setMiddleNames(middleNames);
        }

        // Note: Manager relationship should be set in the service layer
        return staff;
    }
}
