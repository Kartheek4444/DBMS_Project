package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.CustomerSignupDto;
import com.dbmsproject.car_rental.model.Customer;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerSignupDto customerSignupDto);
    CustomerDto getCustomerById(Long customerId);
    List<CustomerDto> getAllCustomers();
    CustomerDto updateCustomer(Long customerId, CustomerDto customerDto);
    Customer findByEmail(String email);
    boolean validateCustomer(String email, String password);
    void deleteCustomer(Long customerId);
    CustomerDto getCustomerByEmail(String email);
}


