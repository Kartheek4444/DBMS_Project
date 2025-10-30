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

    @GetMapping("/bookings/new")
    public String newBookingForm(@RequestParam Long vehicleId, Model model) {
        VehicleDto vehicle = vehicleService.getVehicleById(vehicleId);
        List<CustomerDto> customers = customerService.getAllCustomers();

        BookingDto booking = new BookingDto();
        booking.setVehicleId(vehicleId);
        booking.setStatus(BookingStatus.PENDING);

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
