package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
