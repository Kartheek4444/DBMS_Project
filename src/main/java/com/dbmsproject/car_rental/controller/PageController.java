package com.dbmsproject.car_rental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/home"})
    public String homePage() {
        return "home"; // home.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // signup.html
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard"; // dashboard.html, only accessible after login
    }
}
