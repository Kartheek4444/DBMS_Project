package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("customerUserDetailsService")
@AllArgsConstructor
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));

        return User.builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .authorities(new SimpleGrantedAuthority("USER"))
                .disabled(false)
                .build();
    }
}
