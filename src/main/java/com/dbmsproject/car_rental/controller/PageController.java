package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.CustomerSignupDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import com.dbmsproject.car_rental.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class PageController {

    private final CustomerService customerService;
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
}
