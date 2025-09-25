package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
