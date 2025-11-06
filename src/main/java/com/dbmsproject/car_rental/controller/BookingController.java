package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.model.BookingStatus;
import com.dbmsproject.car_rental.service.BookingService;
import com.dbmsproject.car_rental.service.CustomerService;
import com.dbmsproject.car_rental.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final VehicleService vehicleService;
    private final CustomerService customerService;
    @GetMapping("/bookings/filter")
    public ResponseEntity<List<BookingDto>> filterBookings(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (customerId == null && vehicleId == null && status == null && startDate == null && endDate == null) {
            return ResponseEntity.ok(bookingService.getAllBookings());
        }
        return ResponseEntity.ok(bookingService.filterBookings(customerId, vehicleId, status, startDate, endDate));
    }

    @GetMapping("/bookings/customer/{customerId}")
    public ResponseEntity<List<BookingDto>> getBookingsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(bookingService.getBookingsByCustomerId(customerId));
    }


    @GetMapping("/bookings/vehicle/{vehicleId}")
    public ResponseEntity<List<BookingDto>> getBookingsByVehicle(@RequestParam Long vehicleId) {
        return ResponseEntity.ok(bookingService.getBookingsByVehicleId(vehicleId));
    }

    @PostMapping("/bookings/{id}/confirm")
    public ResponseEntity<BookingDto> confirmBooking(@RequestParam Long bookingId) {
        return ResponseEntity.ok(bookingService.confirmBooking(bookingId));
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }


    @GetMapping("/bookings/{id}/edit")
    public String editBookingForm(@PathVariable Long id, Model model) {
        BookingDto booking = bookingService.getBookingById(id);
        List<VehicleDto> vehicles = vehicleService.getAllVehicles();
        List<CustomerDto> customers = customerService.getAllCustomers();

        model.addAttribute("booking", booking);
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("customers", customers);

        return "booking_edit";
    }

    @PostMapping("/bookings/{id}/update")
    public String updateBooking(@PathVariable Long id, @ModelAttribute BookingDto bookingDto) {
        bookingService.updateBooking(id, bookingDto);
        return "redirect:/bookings";
    }

    @GetMapping("/bookings/new")
    public String newBookingForm(
            @RequestParam Long vehicleId,
            @RequestParam(required = false) Long customerId,
            Authentication authentication,
            Model model) {

        VehicleDto vehicle = vehicleService.getVehicleById(vehicleId);
        List<CustomerDto> customers = customerService.getAllCustomers();

        BookingDto booking = new BookingDto();
        booking.setVehicleId(vehicleId);
        booking.setStatus(BookingStatus.PENDING);

        // Auto-select logged-in customer
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            CustomerDto loggedInCustomer = customerService.getCustomerByEmail(email);

            if (loggedInCustomer != null) {
                booking.setCustomerId(loggedInCustomer.getCustomerId());
                model.addAttribute("selectedCustomerName",
                        loggedInCustomer.getFirstName() + " " + loggedInCustomer.getLastName());
            }
        }
        // Override with URL parameter if provided
        else if (customerId != null) {
            booking.setCustomerId(customerId);
            CustomerDto customer = customerService.getCustomerById(customerId);
            model.addAttribute("selectedCustomerName",
                    customer.getFirstName() + " " + customer.getLastName());
        }

        model.addAttribute("booking", booking);
        model.addAttribute("vehicleId", vehicleId);
        model.addAttribute("vehicleName", vehicle.getMake() + " " + vehicle.getModel());
        model.addAttribute("pricePerDay", vehicle.getPricePerDay());
        model.addAttribute("customers", customers);

        return "new_booking";
    }


    @PostMapping("/bookings/create")
    public String createBooking(@ModelAttribute BookingDto bookingDto) {
        bookingDto.setStatus(BookingStatus.PENDING);
        bookingDto.setBookingDate(LocalDateTime.now());
        bookingService.createBooking(bookingDto);
        return "redirect:/bookings";
    }
}
