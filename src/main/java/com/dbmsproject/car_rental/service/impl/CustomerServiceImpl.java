package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.CustomerSignupDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.CustomerMapper;
import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerDto createCustomer(CustomerSignupDto customerSignupDto) {

        Customer customer = Customer.builder()
                .firstName(customerSignupDto.getFirstName())
                .lastName(customerSignupDto.getLastName())
                .password(passwordEncoder.encode(customerSignupDto.getPassword()))
                .email(customerSignupDto.getEmail())
                .phoneNumber(customerSignupDto.getPhoneNumber())
                .build();
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.toCustomerDto(savedCustomer);
    }

    @Override
    public CustomerDto getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + customerId));
        return CustomerMapper.toCustomerDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream().map(CustomerMapper::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new ResourceNotFoundException("Customer not found with id: " + customerId));
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setLicenseNo(customerDto.getLicenseNo());
        customer.setExpiryDate(customerDto.getExpiryDate());
        customer.setEmail(customerDto.getEmail());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setDno(customerDto.getDno());
        customer.setStreet(customerDto.getStreet());
        customer.setCity(customerDto.getCity());
        customer.setState(customerDto.getState());
        customer.setPinCode(customerDto.getPinCode());
        customer.getMiddleNames().clear();
        if(customerDto.getMiddleNames() != null){
            customer.getMiddleNames().addAll(
                    customerDto.getMiddleNames().stream()
                            .filter(mn -> mn != null && !mn.isBlank())
                            .map(mn -> {
                                var cm = new com.dbmsproject.car_rental.model.CustomerMiddleName();
                                var id = new com.dbmsproject.car_rental.model.CustomerMiddleNameId();
                                id.setCustomerId(customer.getCustomerId());
                                id.setMiddleName(mn);
                                cm.setId(id);
                                cm.setCustomer(customer);
                                return cm;
                            })
                            .toList()
            );
        }
        Customer updatedCustomer = customerRepository.save(customer);

        return CustomerMapper.toCustomerDto(updatedCustomer);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
    }

    @Override
    public boolean validateCustomer(String email, String rawPassword) {
        // Find customer by email
        return customerRepository.findByEmail(email)
                .map(customer -> passwordEncoder.matches(rawPassword, customer.getPassword()))
                .orElse(false); // email not found
    }


    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() ->
                new ResourceNotFoundException("Customer not found with id: " + customerId));
        customerRepository.delete(customer);
    }
}
