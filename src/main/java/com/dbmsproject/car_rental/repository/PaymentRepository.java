package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
