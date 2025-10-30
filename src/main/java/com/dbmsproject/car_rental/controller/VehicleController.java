package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class VehicleController {
    private VehicleService vehicleService;

    @GetMapping("/vehicles/register")
    public String showRegisterVehicleForm(Model model) {
        model.addAttribute("vehicle", new VehicleDto());
        return "register_vehicle";
    }

    @PostMapping("/vehicles/register")
    public String registerVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register_vehicle";
        }

        try {
            vehicleService.createVehicle(vehicleDto);
            redirectAttributes.addFlashAttribute("success", "Vehicle registered successfully!");
            return "redirect:/vehicles";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to register vehicle: " + e.getMessage());
            return "redirect:/vehicles/register";
        }
    }

    @GetMapping("/vehicles")
    public String showVehicles(Model model) {
        List<VehicleDto> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "vehicles";
    }

    @GetMapping("/vehicles/{id}")
    public String showVehicleDetail(@PathVariable Long id, Model model) {
        VehicleDto vehicle = vehicleService.getVehicleById(id);
        model.addAttribute("vehicle", vehicle);
        return "vehicle_detail";
    }
}
