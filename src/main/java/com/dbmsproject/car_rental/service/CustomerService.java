package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.CustomerSignupDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerSignupDto customerSignupDto);
    CustomerDto getCustomerById(Long customerId);
    List<CustomerDto> getAllCustomers();
    CustomerDto updateCustomer(Long customerId, CustomerDto customerDto);
    void deleteCustomer(Long customerId);
}
