package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find customer first
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer != null) {
            return User.builder()
                    .username(customer.getEmail())
                    .password(customer.getPassword())
                    .authorities(new SimpleGrantedAuthority("USER"))
                    .build();
        }

        // Try to find staff
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return User.builder()
                .username(staff.getEmail())
                .password(staff.getPassword())
                .authorities(new SimpleGrantedAuthority(staff.getRole().name()))
                .build();
    }
}
