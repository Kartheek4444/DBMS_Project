package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.StaffDto;
import com.dbmsproject.car_rental.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    public ResponseEntity<StaffDto> createStaff(@RequestBody StaffDto staffDto) {
        StaffDto savedStaff = staffService.createStaff(staffDto);
        return new ResponseEntity<>(savedStaff, HttpStatus.CREATED);
    }

    @GetMapping("/staff/{id}")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable("id") Long staffId) {
        StaffDto staffDto = staffService.getStaffById(staffId);
        return ResponseEntity.ok(staffDto);
    }

    @GetMapping
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        List<StaffDto> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<StaffDto> updateStaff(@PathVariable("id") Long staffId, @RequestBody StaffDto staffDto) {
        StaffDto updatedStaff = staffService.updateStaff(staffId, staffDto);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<String> deleteStaff(@PathVariable("id") Long staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.ok("Staff deleted successfully");
    }

    @PostMapping("/staff/register")
    public String registerStaff(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String position,
            @RequestParam LocalDate hireDate,
            @RequestParam(required = false) Long managerId,
            RedirectAttributes redirectAttributes) {

        try {
            StaffDto staffDto = StaffDto.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .position(position)
                    .hireDate(hireDate)
                    .managerId(managerId)
                    .isActive(true)
                    .build();

            staffService.createStaff(staffDto);
            redirectAttributes.addFlashAttribute("successMessage", "Staff registered successfully!");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/dashboard";
        }
    }
}
