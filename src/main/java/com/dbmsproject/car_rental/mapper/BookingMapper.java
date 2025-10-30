package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.model.Booking;

public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingDto.builder()
                .bookingId(booking.getBookingId())
                .customerId(booking.getCustomer().getCustomerId())
                .customerName(booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName())
                .vehicleId(booking.getVehicle().getVehicleId())
                .vehicleMake(booking.getVehicle().getMake())
                .vehicleModel(booking.getVehicle().getModel())
                .pickupDate(booking.getPickupDate())
                .returnDate(booking.getReturnDate())
                .bookingDate(booking.getBookingDate())
                .status(booking.getStatus())
                .depositAmount(booking.getDepositAmount())
                .build();
    }

    public static Booking toEntity(BookingDto dto) {
        if (dto == null) {
            return null;
        }

        return Booking.builder()
                .bookingId(dto.getBookingId())
                .pickupDate(dto.getPickupDate())
                .returnDate(dto.getReturnDate())
                .bookingDate(dto.getBookingDate())
                .status(dto.getStatus())
                .depositAmount(dto.getDepositAmount())
                .build();
    }
}
