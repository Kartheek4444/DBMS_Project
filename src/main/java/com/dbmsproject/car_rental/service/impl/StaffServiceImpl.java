package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.StaffDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.StaffMapper;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.model.StaffMiddleName;
import com.dbmsproject.car_rental.model.StaffMiddleNameId;
import com.dbmsproject.car_rental.repository.StaffRepository;
import com.dbmsproject.car_rental.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    @Transactional
    public StaffDto createStaff(StaffDto staffDto) {
        Staff staff = StaffMapper.toStaff(staffDto);

        // Set manager if managerId is provided
        if (staffDto.getManagerId() != null) {
            Staff manager = staffRepository.findById(staffDto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + staffDto.getManagerId()));
            staff.setManager(manager);
        }

        Staff savedStaff = staffRepository.save(staff);

        // Handle middle names after saving staff (to get staffId)
        if (staffDto.getMiddleNames() != null && !staffDto.getMiddleNames().isEmpty()) {
            Staff finalSavedStaff = savedStaff;
            List<StaffMiddleName> middleNames = staffDto.getMiddleNames().stream()
                    .filter(Objects::nonNull)
                    .map(mn -> {
                        StaffMiddleName smn = new StaffMiddleName();
                        StaffMiddleNameId id = new StaffMiddleNameId();
                        id.setStaffId(finalSavedStaff.getStaffId());
                        id.setMiddleName(mn);
                        smn.setId(id);
                        smn.setStaff(finalSavedStaff);
                        return smn;
                    })
                    .collect(Collectors.toList());
            savedStaff.setMiddleNames(middleNames);
            savedStaff = staffRepository.save(savedStaff);
        }

        return StaffMapper.toStaffDto(savedStaff);
    }

    @Override
    @Transactional(readOnly = true)
    public StaffDto getStaffById(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));
        return StaffMapper.toStaffDto(staff);
    }

    @Override
    @Transactional
    public StaffDto updateStaff(Long staffId, StaffDto staffDto) {
        Staff existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));

        // Update basic fields
        existingStaff.setFirstName(staffDto.getFirstName());
        existingStaff.setLastName(staffDto.getLastName());
        existingStaff.setIsActive(staffDto.getIsActive());
        existingStaff.setEmail(staffDto.getEmail());
        existingStaff.setPhone(staffDto.getPhone());
        existingStaff.setPosition(staffDto.getPosition());
        existingStaff.setHireDate(staffDto.getHireDate());

        // Update manager
        if (staffDto.getManagerId() != null) {
            Staff manager = staffRepository.findById(staffDto.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + staffDto.getManagerId()));
            existingStaff.setManager(manager);
        } else {
            existingStaff.setManager(null);
        }

        // Update middle names
        existingStaff.getMiddleNames().clear();
        if (staffDto.getMiddleNames() != null && !staffDto.getMiddleNames().isEmpty()) {
            List<StaffMiddleName> middleNames = staffDto.getMiddleNames().stream()
                    .filter(Objects::nonNull)
                    .map(mn -> {
                        StaffMiddleName smn = new StaffMiddleName();
                        StaffMiddleNameId id = new StaffMiddleNameId();
                        id.setStaffId(existingStaff.getStaffId());
                        id.setMiddleName(mn);
                        smn.setId(id);
                        smn.setStaff(existingStaff);
                        return smn;
                    })
                    .toList();
            existingStaff.getMiddleNames().addAll(middleNames);
        }

        Staff updatedStaff = staffRepository.save(existingStaff);
        return StaffMapper.toStaffDto(updatedStaff);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateStaff(String email, String password) {
        // Note: This is a placeholder implementation
        // You should implement proper password hashing and validation
        return staffRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public void deleteStaff(Long staffId) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: " + staffId));
        staffRepository.delete(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDto> getAllStaff() {
        return staffRepository.findAll().stream()
                .map(StaffMapper::toStaffDto)
                .collect(Collectors.toList());
    }
}
