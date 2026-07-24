package com.dbmsproject.car_rental.security;

import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.model.Roles;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnifiedUserDetailsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private UnifiedUserDetailsService userDetailsService;

    private Customer testCustomer;
    private Staff testStaff;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .customerId(1L)
                .email("user@example.com")
                .password("encodedCustomerPass")
                .firstName("Customer")
                .lastName("User")
                .phoneNumber("1112223333")
                .build();

        testStaff = Staff.builder()
                .staffId(2L)
                .email("staff@example.com")
                .password("encodedStaffPass")
                .firstName("Staff")
                .lastName("Member")
                .role(Roles.ADMIN)
                .isActive(true)
                .build();
    }

    @Test
    void loadUserByUsername_CustomerFound() {
        when(customerRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testCustomer));

        UserDetails userDetails = userDetailsService.loadUserByUsername("user@example.com");

        assertNotNull(userDetails);
        assertEquals("user@example.com", userDetails.getUsername());
        assertEquals("encodedCustomerPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("USER")));
        verify(customerRepository, times(1)).findByEmail("user@example.com");
        verify(staffRepository, never()).findByEmail(any());
    }

    @Test
    void loadUserByUsername_StaffFound() {
        when(customerRepository.findByEmail("staff@example.com")).thenReturn(Optional.empty());
        when(staffRepository.findByEmail("staff@example.com")).thenReturn(Optional.of(testStaff));

        UserDetails userDetails = userDetailsService.loadUserByUsername("staff@example.com");

        assertNotNull(userDetails);
        assertEquals("staff@example.com", userDetails.getUsername());
        assertEquals("encodedStaffPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN")));
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_StaffDisabled() {
        testStaff.setIsActive(false);
        when(customerRepository.findByEmail("staff@example.com")).thenReturn(Optional.empty());
        when(staffRepository.findByEmail("staff@example.com")).thenReturn(Optional.of(testStaff));

        UserDetails userDetails = userDetailsService.loadUserByUsername("staff@example.com");

        assertNotNull(userDetails);
        assertFalse(userDetails.isEnabled());
    }

    @Test
    void loadUserByUsername_NotFound_ThrowsException() {
        when(customerRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        when(staffRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("unknown@example.com")
        );
    }
}
