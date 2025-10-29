package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.CustomerSignupDto;
import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.dbmsproject.car_rental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import com.dbmsproject.car_rental.model.Vehicle;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PageController {

    private final CustomerService customerService;
    private final VehicleService vehicleService;

    @GetMapping({"/", "/index"})
    public String homePage() {
        return "index"; // home.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("username") String email,
            @RequestParam("password") String password,
            HttpServletRequest request,
            Model model
    ) {
        try {
            boolean isValid = customerService.validateCustomer(email, password);
            if (isValid) {
                // Create session for logged-in user
                HttpSession session = request.getSession();
                session.setAttribute("userEmail", email);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("loginError", "Invalid email or password");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("loginError", "An error occurred: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("customerSignupDto", new CustomerSignupDto());
        return "signup"; // signup.html
    }

    @PostMapping("/signup")
    public String handleSignup(
            @Valid @ModelAttribute("customerSignupDto") CustomerSignupDto dto,
            BindingResult br,
            Model model) {
        if (br.hasErrors()) return "signup";

        try {
            customerService.createCustomer(dto);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("signupError", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard"; // dashboard.html, only accessible after login
    }


    @GetMapping("/vehicle_detail")
    public String vehicleDetailPage() {
        return "vehicle_detail"; // vehicle_detail.html
    }
    @GetMapping("/bookings")
    public String bookingsPage() {
        return "bookings"; // bookings.html

    }

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
        if (vehicle == null) {
            return "redirect:/vehicles";
        }
        model.addAttribute("vehicle", vehicle);
        return "vehicle_detail";
    }

}
