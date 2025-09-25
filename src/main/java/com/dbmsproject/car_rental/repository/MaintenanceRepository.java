package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
