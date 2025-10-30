package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomer_CustomerId(Long customerId);

    List<Booking> findByVehicle_VehicleId(Long vehicleId);

    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByPickupDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Booking> findByPickupDateBeforeAndReturnDateAfter(LocalDateTime pickupBefore, LocalDateTime returnAfter);

    List<Booking> findByPickupDateAfter(LocalDateTime pickupDate);

    List<Booking> findByReturnDateBefore(LocalDateTime returnDate);

    @Query("SELECT b FROM Booking b WHERE " +
            "(:customerId IS NULL OR b.customer.customerId = :customerId) AND " +
            "(:vehicleId IS NULL OR b.vehicle.vehicleId = :vehicleId) AND " +
            "(:status IS NULL OR b.status = :status) AND " +
            "(:startDate IS NULL OR b.pickupDate >= :startDate) AND " +
            "(:endDate IS NULL OR b.pickupDate <= :endDate)")
    List<Booking> filterBookings(
            @Param("customerId") Long customerId,
            @Param("vehicleId") Long vehicleId,
            @Param("status") BookingStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

}
