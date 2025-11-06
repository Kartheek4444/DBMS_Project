package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.dto.CustomerSignupDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.CustomerMapper;
import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.repository.StaffRepository;
import com.dbmsproject.car_rental.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private StaffRepository staffRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public CustomerDto createCustomer(CustomerSignupDto customerSignupDto) {
        // Check if email already exists in customer table
        if (customerRepository.findByEmail(customerSignupDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email is already registered. Please use a different email or login.");
        }

        // Check if email already exists in staff table
        if (staffRepository.findByEmail(customerSignupDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email is already registered. Please use a different email or login.");
        }

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
    @Transactional
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setLicenseNo(customerDto.getLicenseNo());
        customer.setExpiryDate(customerDto.getExpiryDate());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setDno(customerDto.getDno());
        customer.setStreet(customerDto.getStreet());
        customer.setCity(customerDto.getCity());
        customer.setState(customerDto.getState());
        customer.setPinCode(customerDto.getPinCode());

        // Handle middle names
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
    @Transactional
    public CustomerDto updateCustomerProfile(Long customerId, CustomerDto customerDto, MultipartFile avatarFile) throws IOException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setDateOfBirth(customerDto.getDateOfBirth());
        customer.setLicenseNo(customerDto.getLicenseNo());
        customer.setExpiryDate(customerDto.getExpiryDate());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setDno(customerDto.getDno());
        customer.setStreet(customerDto.getStreet());
        customer.setCity(customerDto.getCity());
        customer.setState(customerDto.getState());
        customer.setPinCode(customerDto.getPinCode());

        // Handle middle names
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

        // Handle avatar upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String uploadDir = "uploads/avatars/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = customerId + "_" + System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            customer.setAvatarUrl("/" + uploadDir + fileName);
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

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return CustomerMapper.toCustomerDto(customer);
    }
}
