package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);

    Staff getStaffByEmail(String email);
}
