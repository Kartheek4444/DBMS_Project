package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Maintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    Page<Maintenance> findAllByVehicleVehicleId(Long vehicleId, Pageable pageable);
    Page<Maintenance> findAllByHandledByStaffId(Long staffId, Pageable pageable);
}
