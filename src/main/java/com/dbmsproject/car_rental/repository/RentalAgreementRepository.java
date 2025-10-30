package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {
    List<RentalAgreement> findByBooking_BookingId(Long bookingId);
    List<RentalAgreement> findByHandledBy_StaffId(Long staffId);
    List<RentalAgreement> findByPickupTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<RentalAgreement> findByReturnTimeIsNull();
    List<RentalAgreement> findByReturnTimeIsNotNull();

    @Query("SELECT ra FROM RentalAgreement ra WHERE " +
            "(:bookingId IS NULL OR ra.booking.bookingId = :bookingId) AND " +
            "(:staffId IS NULL OR ra.handledBy.staffId = :staffId) AND " +
            "(:pickupStartDate IS NULL OR ra.pickupTime >= :pickupStartDate) AND " +
            "(:pickupEndDate IS NULL OR ra.pickupTime <= :pickupEndDate) AND " +
            "(:returnStartDate IS NULL OR ra.returnTime >= :returnStartDate) AND " +
            "(:returnEndDate IS NULL OR ra.returnTime <= :returnEndDate) AND " +
            "(:isCompleted IS NULL OR (:isCompleted = true AND ra.returnTime IS NOT NULL) OR (:isCompleted = false AND ra.returnTime IS NULL))")
    List<RentalAgreement> filterRentalAgreements(
            @Param("bookingId") Long bookingId,
            @Param("staffId") Long staffId,
            @Param("pickupStartDate") LocalDateTime pickupStartDate,
            @Param("pickupEndDate") LocalDateTime pickupEndDate,
            @Param("returnStartDate") LocalDateTime returnStartDate,
            @Param("returnEndDate") LocalDateTime returnEndDate,
            @Param("isCompleted") Boolean isCompleted);
}
