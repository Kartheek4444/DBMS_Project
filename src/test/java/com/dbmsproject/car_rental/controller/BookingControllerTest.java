package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.VehicleDto;
import com.dbmsproject.car_rental.model.BookingStatus;
import com.dbmsproject.car_rental.service.BookingService;
import com.dbmsproject.car_rental.service.CustomerService;
import com.dbmsproject.car_rental.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private BookingController bookingController;

    private BookingDto bookingDto;
    private VehicleDto vehicleDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();

        bookingDto = BookingDto.builder()
                .bookingId(1L)
                .customerId(10L)
                .vehicleId(20L)
                .status(BookingStatus.PENDING)
                .pickupDate(LocalDateTime.now().plusDays(1))
                .returnDate(LocalDateTime.now().plusDays(3))
                .build();

        vehicleDto = VehicleDto.builder()
                .vehicleId(20L)
                .make("Toyota")
                .model("Camry")
                .pricePerDay(new BigDecimal("50.00"))
                .build();
    }

    @Test
    void getBookingById_ReturnsBooking() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(1))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void confirmBooking_ReturnsConfirmedBooking() throws Exception {
        bookingDto.setStatus(BookingStatus.CONFIRMED);
        when(bookingService.confirmBooking(1L)).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings/1/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void filterBookings_ReturnsList() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(1));
    }

    @Test
    void newBookingForm_ReturnsView() throws Exception {
        when(vehicleService.getVehicleById(20L)).thenReturn(vehicleDto);
        when(customerService.getAllCustomers()).thenReturn(List.of(new CustomerDto()));

        mockMvc.perform(get("/bookings/new").param("vehicleId", "20"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_booking"))
                .andExpect(model().attributeExists("booking", "vehicleName", "pricePerDay"));
    }

    @Test
    void createBooking_RedirectsToBookings() throws Exception {
        when(bookingService.createBooking(any(BookingDto.class))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings/create")
                        .param("vehicleId", "20")
                        .param("customerId", "10")
                        .param("pickupDate", "2026-08-01T10:00")
                        .param("returnDate", "2026-08-03T10:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings"));

        verify(bookingService).createBooking(any(BookingDto.class));
    }
}
