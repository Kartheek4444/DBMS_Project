package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.StaffDto;
import com.dbmsproject.car_rental.service.StaffService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/staff")
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        List<StaffDto> staff = staffService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/staff/{id}/edit")
    public String editStaffPage(@PathVariable("id") Long staffId, Model model) {
        StaffDto staff = staffService.getStaffById(staffId);
        model.addAttribute("staff", staff);
        return "staff_edit";
    }

    @PostMapping("/staff/{id}/update")
    public String updateStaff(
            @PathVariable("id") Long staffId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String position,
            @RequestParam LocalDate hireDate,
            @RequestParam(required = false) Long managerId,
            @RequestParam(required = false) String password,
            @RequestParam Boolean isActive,
            RedirectAttributes redirectAttributes) {

        try {
            StaffDto existingStaff = staffService.getStaffById(staffId);

            StaffDto staffDto = StaffDto.builder()
                    .staffId(staffId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .password(password)
                    .position(position)
                    .hireDate(hireDate)
                    .managerId(managerId)
                    .isActive(isActive)
                    .password(password != null && !password.isEmpty() ? password : existingStaff.getPassword())
                    .build();

            staffService.updateStaff(staffId, staffDto);
            redirectAttributes.addFlashAttribute("successMessage", "Staff updated successfully!");
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Update failed: " + e.getMessage());
            return "redirect:/staff/" + staffId + "/edit";
        }
    }


    @PostMapping("/staff/register")
    public String registerStaff(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam String position,
            @RequestParam LocalDate hireDate,
            @RequestParam(required = false) Long managerId,
            @RequestParam String role,
            RedirectAttributes redirectAttributes) {

        try {
            StaffDto staffDto = StaffDto.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .password(password)
                    .position(position)
                    .hireDate(hireDate)
                    .managerId(managerId)
                    .role(role)
                    .isActive(true)
                    .build();

            staffService.createStaff(staffDto);
            redirectAttributes.addFlashAttribute("successMessage", "Staff registered successfully!");
            return "redirect:/staff/dashboard";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }
}
