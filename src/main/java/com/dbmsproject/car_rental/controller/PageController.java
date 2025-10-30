package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.CustomerSignupDto;
import com.dbmsproject.car_rental.dto.StaffDto;
import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.BookingStatus;
import com.dbmsproject.car_rental.service.BookingService;
import com.dbmsproject.car_rental.service.CustomerService;
import com.dbmsproject.car_rental.service.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class PageController {

    private final CustomerService customerService;
    private final StaffService staffService;
    private final BookingService bookingService;

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
    public String dashboardPage(
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Model model) {

        List<BookingDto> bookings;

        if (status != null || startDate != null || endDate != null) {
            LocalDateTime start = startDate != null ? startDate.toLocalDate().atStartOfDay() : null;
            LocalDateTime end = endDate != null ? endDate.toLocalDate().atTime(23, 59, 59) : null;
            BookingStatus bookingStatus = status;

            bookings = bookingService.filterBookings(null, null, bookingStatus, start, end);
        } else {
            bookings = bookingService.getAllBookings();
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedStartDate", startDate);
        model.addAttribute("selectedEndDate", endDate);

        return "dashboard";
    }


    @GetMapping("/bookings")
    public String bookingsPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            CustomerDto customer = customerService.getCustomerByEmail(email);

            if (customer != null) {
                List<BookingDto> bookings = bookingService.getBookingsByCustomerId(customer.getCustomerId());
                model.addAttribute("bookings", bookings);
            } else {
                model.addAttribute("bookings", new ArrayList<>());
            }
        } else {
            model.addAttribute("bookings", new ArrayList<>());
        }

        return "bookings";
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        List<StaffDto> staffList = staffService.getAllStaff();

        long activeCount = staffList.stream().filter(StaffDto::getIsActive).count();
        long inactiveCount = staffList.size() - activeCount;

        model.addAttribute("staffList", staffList);
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("inactiveCount", inactiveCount);

        return "admin_dashboard";
    }
}
