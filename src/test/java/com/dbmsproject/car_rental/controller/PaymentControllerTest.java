package com.dbmsproject.car_rental.controller;

import com.dbmsproject.car_rental.dto.PaymentDto;
import com.dbmsproject.car_rental.model.PaymentMethod;
import com.dbmsproject.car_rental.model.PaymentStatus;
import com.dbmsproject.car_rental.model.PaymentType;
import com.dbmsproject.car_rental.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentDto paymentDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();

        paymentDto = PaymentDto.builder()
                .paymentId(1L)
                .bookingId(10L)
                .amount(new BigDecimal("199.99"))
                .paymentType(PaymentType.RENTAL_FEE)
                .paymentMethod(PaymentMethod.CARD)
                .transactionDate(LocalDateTime.now())
                .status(PaymentStatus.COMPLETED)
                .build();
    }

    @Test
    void getPaymentById_ReturnsPayment() throws Exception {
        when(paymentService.getPaymentById(1L)).thenReturn(paymentDto);

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1))
                .andExpect(jsonPath("$.amount").value(199.99))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void getPaymentByBookingId_ReturnsPayment() throws Exception {
        when(paymentService.getPaymentByBookingId(10L)).thenReturn(paymentDto);

        mockMvc.perform(get("/api/payments/booking/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(10));
    }

    @Test
    void getAllPayments_ReturnsList() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of(paymentDto));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].paymentId").value(1));
    }

    @Test
    void updatePaymentStatus_ReturnsUpdatedPayment() throws Exception {
        paymentDto.setStatus(PaymentStatus.REFUNDED);
        when(paymentService.updatePaymentStatus(eq(1L), eq(PaymentStatus.REFUNDED))).thenReturn(paymentDto);

        mockMvc.perform(put("/api/payments/1/status").param("status", "REFUNDED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REFUNDED"));
    }

    @Test
    void createPayment_ReturnsCreated() throws Exception {
        when(paymentService.createPayment(any(PaymentDto.class))).thenReturn(paymentDto);

        String jsonPayload = """
                {
                    "bookingId": 10,
                    "amount": 199.99,
                    "paymentType": "RENTAL_FEE",
                    "paymentMethod": "CARD"
                }
                """;

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(1));
    }
}
