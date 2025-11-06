// language: java
package com.dbmsproject.car_rental.security;

import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UnifiedUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // try customers first
        Customer customer = customerRepository.findByEmail(username).orElse(null);
        if (customer != null) {
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("USER"));
            return User.withUsername(customer.getEmail())
                    .password(customer.getPassword())
                    .authorities(authorities)
                    .accountLocked(false)
                    .disabled(false)
                    .build();
        }

        // then staff
        Staff staff = staffRepository.findByEmail(username).orElse(null);
        if (staff != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            // map staff.role (e.g. "ADMIN" or "STAFF") to authority
            authorities.add(new SimpleGrantedAuthority(staff.getRole().name()));
            return User.withUsername(staff.getEmail())
                    .password(staff.getPassword())
                    .authorities(authorities)
                    .accountLocked(false)
                    .disabled(staff.getIsActive() != null && !staff.getIsActive())
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
