package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.CustomerSignupDto;
import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerSignupDto signupDto;

    @BeforeEach
    void setUp() {
        signupDto = CustomerSignupDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("1234567890")
                .password("password123")
                .build();
    }

    @Test
    void createCustomer_Success() {
        when(customerRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.empty());
        when(staffRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByPhoneNumber(signupDto.getPhoneNumber())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signupDto.getPassword())).thenReturn("encodedPassword");

        Customer savedCustomer = Customer.builder()
                .customerId(1L)
                .firstName(signupDto.getFirstName())
                .lastName(signupDto.getLastName())
                .email(signupDto.getEmail())
                .phoneNumber(signupDto.getPhoneNumber())
                .password("encodedPassword")
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDto result = customerService.createCustomer(signupDto);

        assertNotNull(result);
        assertEquals(1L, result.getCustomerId());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void createCustomer_DuplicateEmail_ThrowsException() {
        when(customerRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.of(new Customer()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.createCustomer(signupDto)
        );

        assertTrue(exception.getMessage().contains("email is already registered"));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void createCustomer_DuplicatePhoneNumber_ThrowsException() {
        when(customerRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.empty());
        when(staffRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByPhoneNumber(signupDto.getPhoneNumber())).thenReturn(Optional.of(new Customer()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                customerService.createCustomer(signupDto)
        );

        assertTrue(exception.getMessage().contains("phone number is already registered"));
        verify(customerRepository, never()).save(any());
    }
}
