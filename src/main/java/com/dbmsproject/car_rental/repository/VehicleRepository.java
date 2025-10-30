package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.model.VehicleCategory;
import com.dbmsproject.car_rental.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("""
        SELECT v FROM Vehicle v
        WHERE (:category IS NULL OR v.category = :category)
          AND (:status IS NULL OR v.status = :status)
          AND (:minPrice IS NULL OR v.pricePerDay >= :minPrice)
          AND (:maxPrice IS NULL OR v.pricePerDay <= :maxPrice)
        """)
    List<Vehicle> findByFilters(
            @Param("category") VehicleCategory category,
            @Param("status") VehicleStatus status,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );
}