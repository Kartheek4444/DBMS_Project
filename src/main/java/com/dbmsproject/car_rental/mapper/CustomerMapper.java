package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.CustomerDto;
import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.model.CustomerMiddleName;
import com.dbmsproject.car_rental.model.CustomerMiddleNameId;

import java.util.*;

public class CustomerMapper {
    public static CustomerDto toCustomerDto(Customer customer) {

        List<String> middleNames = Optional.ofNullable(customer.getMiddleNames())
                .orElse(Collections.emptyList())
                .stream()
                .map(cm -> cm.getId() == null ? null : cm.getId().getMiddleName())
                .filter(Objects::nonNull)
                .toList();

        return CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .avatarUrl(customer.getAvatarUrl())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .password(customer.getPassword())
                .dateOfBirth(customer.getDateOfBirth())
                .licenseNo(customer.getLicenseNo())
                .expiryDate(customer.getExpiryDate())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .dno(customer.getDno())
                .street(customer.getStreet())
                .city(customer.getCity())
                .state(customer.getState())
                .pinCode(customer.getPinCode())
                .registrationDate(customer.getRegistrationDate())
                .middleNames(middleNames.isEmpty() ? null : middleNames)
                .build();
    }

    public static Customer toCustomer(CustomerDto customerDto){
        Customer customer = Customer.builder()
                .avatarUrl(customerDto.getAvatarUrl())
                .firstName(customerDto.getFirstName())
                .lastName(customerDto.getLastName())
                .password(customerDto.getPassword())
                .dateOfBirth(customerDto.getDateOfBirth())
                .licenseNo(customerDto.getLicenseNo())
                .expiryDate(customerDto.getExpiryDate())
                .email(customerDto.getEmail())
                .phoneNumber(customerDto.getPhoneNumber())
                .dno(customerDto.getDno())
                .street(customerDto.getStreet())
                .city(customerDto.getCity())
                .state(customerDto.getState())
                .pinCode(customerDto.getPinCode())
                .build();

        if (customer.getBookings() == null) customer.setBookings(new ArrayList<>());
        if (customer.getMiddleNames() == null) customer.setMiddleNames(new ArrayList<>());

        if(customerDto.getMiddleNames() != null){
            List<CustomerMiddleName> middleNames = customerDto.getMiddleNames().stream()
                    .filter(Objects::nonNull)
                    .map(mn -> {
                        CustomerMiddleName cm = new CustomerMiddleName();
                        CustomerMiddleNameId id = new CustomerMiddleNameId();
                        id.setCustomerId(customer.getCustomerId());
                        id.setMiddleName(mn);
                        cm.setId(id);
                        cm.setCustomer(customer);
                        return cm;
                    }).toList();
            customer.setMiddleNames(middleNames);
        }
        return customer;
    }
}
