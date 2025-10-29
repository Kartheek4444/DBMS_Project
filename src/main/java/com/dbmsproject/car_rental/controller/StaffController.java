package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.StaffDto;
import com.dbmsproject.car_rental.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@AllArgsConstructor
public class StaffController {

    private StaffService staffService;

    @PostMapping
    public ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto staffDto) {
        StaffDto savedStaff = staffService.createStaff(staffDto);
        return new ResponseEntity<>(savedStaff, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable("id") Long staffId) {
        StaffDto staffDto = staffService.getStaffById(staffId);
        return ResponseEntity.ok(staffDto);
    }

    @GetMapping
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        List<StaffDto> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDto> updateStaff(@PathVariable("id") Long staffId, @RequestBody StaffDto staffDto) {
        StaffDto updatedStaff = staffService.updateStaff(staffId, staffDto);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable("id") Long staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok("Staff deleted successfully");
    }
}
