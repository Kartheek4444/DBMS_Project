package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.RentalAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, Long> {
}
