package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
}
