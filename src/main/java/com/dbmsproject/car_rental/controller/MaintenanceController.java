package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.MaintenanceDto;
import com.dbmsproject.car_rental.service.MaintenanceService;
import com.dbmsproject.car_rental.service.VehicleService;
import com.dbmsproject.car_rental.service.StaffService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@AllArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;
    private final VehicleService vehicleService;
    private final StaffService staffService;

    @GetMapping("/maintenance")
    public String getMaintenancePage(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long staffId,
            Model model) {

        List<MaintenanceDto> maintenanceList;
        Pageable pageable = PageRequest.of(0, 100);

        if (vehicleId != null) {
            Page<MaintenanceDto> page = maintenanceService.getVehicleMaintenance(vehicleId, pageable);
            maintenanceList = page.getContent();
            model.addAttribute("selectedVehicleId", vehicleId);
        } else if (staffId != null) {
            Page<MaintenanceDto> page = maintenanceService.getStaffMaintenance(staffId, pageable);
            maintenanceList = page.getContent();
            model.addAttribute("selectedStaffId", staffId);
        } else {
            Page<MaintenanceDto> page = maintenanceService.getAllMaintenance(pageable);
            maintenanceList = page.getContent();
        }

        model.addAttribute("maintenanceList", maintenanceList);
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("staffList", staffService.getAllStaff());

        return "maintenance_page";
    }


    @GetMapping("/maintenance/{id}")
    public String getMaintenanceDetails(@PathVariable Long id, Model model) {
        MaintenanceDto maintenance = maintenanceService.getMaintenance(id);
        model.addAttribute("maintenance", maintenance);
        return "maintenance_details";
    }

    @GetMapping("/maintenance/add")
    public String showAddMaintenanceForm(Model model) {
        model.addAttribute("maintenanceDto", new MaintenanceDto());
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("staffList", staffService.getAllStaff());
        model.addAttribute("isEdit", false);
        return "new_maintenance";
    }

    @PostMapping("/maintenance/create")
    public String createMaintenance(@Valid @ModelAttribute MaintenanceDto maintenanceDto,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            model.addAttribute("staffList", staffService.getAllStaff());
            model.addAttribute("isEdit", false);
            return "new_maintenance";
        }

        try {
            MaintenanceDto created = maintenanceService.createMaintenance(maintenanceDto);
            redirectAttributes.addFlashAttribute("success", "Maintenance record created successfully!");
            return "redirect:/maintenance";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to create maintenance record: " + e.getMessage());
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            model.addAttribute("staffList", staffService.getAllStaff());
            model.addAttribute("isEdit", false);
            return "new_maintenance";
        }
    }

    @GetMapping("/maintenance/edit/{id}")
    public String showEditMaintenanceForm(@PathVariable Long id, Model model) {
        MaintenanceDto maintenance = maintenanceService.getMaintenance(id);
        model.addAttribute("maintenanceDto", maintenance);
        model.addAttribute("vehicles", vehicleService.getAllVehicles());
        model.addAttribute("staffList", staffService.getAllStaff());
        model.addAttribute("isEdit", true);
        return "new_maintenance";
    }

    @PostMapping("/maintenance/update/{id}")
    public String updateMaintenance(@PathVariable Long id,
                                    @Valid @ModelAttribute MaintenanceDto maintenanceDto,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            model.addAttribute("staffList", staffService.getAllStaff());
            model.addAttribute("isEdit", true);
            return "new_maintenance";
        }

        try {
            MaintenanceDto updated = maintenanceService.updateMaintenance(id, maintenanceDto);
            redirectAttributes.addFlashAttribute("success", "Maintenance record updated successfully!");
            return "redirect:/maintenance/" + updated.getMaintenanceId();
        } catch (Exception e) {
            model.addAttribute("error", "Failed to update maintenance record: " + e.getMessage());
            model.addAttribute("vehicles", vehicleService.getAllVehicles());
            model.addAttribute("staffList", staffService.getAllStaff());
            model.addAttribute("isEdit", true);
            return "new_maintenance";
        }
    }

    @PostMapping("/maintenance/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            maintenanceService.deleteMaintenance(id);
            redirectAttributes.addFlashAttribute("success", "Maintenance record deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete maintenance record: " + e.getMessage());
        }
        return "redirect:/maintenance";
    }
}
