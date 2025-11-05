package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.model.Staff;
import com.dbmsproject.car_rental.repository.StaffRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("staffUserDetailsService")
@AllArgsConstructor
public class StaffDetailsService implements UserDetailsService {

    private final StaffRepository staffRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Staff staff = staffRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Staff not found with email: " + email));

        // Check if email is verified (if you implement verification later)
        boolean enabled = Boolean.TRUE.equals(staff.getIsActive());

        return User.builder()
                .username(staff.getEmail())
                .password(staff.getPassword())
                .authorities(new SimpleGrantedAuthority(staff.getRole().name()))
                .disabled(!enabled)
                .build();
    }
}
