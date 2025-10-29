package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.model.VehicleCategory;
import com.dbmsproject.car_rental.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByStatus(VehicleStatus status);
    List<Vehicle> findByCategory(VehicleCategory vehicleCategory);
    List<Vehicle> findByPricePerDayBetween(BigDecimal bigDecimal, BigDecimal bigDecimal1);
    List<Vehicle> findByMake(String make);
}
