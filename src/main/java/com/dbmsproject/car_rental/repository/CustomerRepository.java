package com.dbmsproject.car_rental.repository;

import com.dbmsproject.car_rental.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
