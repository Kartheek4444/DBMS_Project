package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class VehicleController {
    private VehicleService vehicleService;
    private static final String UPLOAD_DIR = "uploads/vehicles/";

    @GetMapping("/vehicles/register")
    public String showRegisterVehicleForm(Model model) {
        model.addAttribute("vehicle", new VehicleDto());
        return "register_vehicle";
    }


    @PostMapping("/vehicles/register")
    public String registerVehicle(@ModelAttribute VehicleDto vehicleDto,
                                  @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                vehicleDto.setImageUrl(imagePath);
            }
            vehicleService.createVehicle(vehicleDto);
            return "redirect:/vehicles";
        } catch (IOException e) {
            return "redirect:/vehicles/register?error=upload";
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        Path filepath = Paths.get(UPLOAD_DIR + filename);
        Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/vehicles/" + filename;
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
