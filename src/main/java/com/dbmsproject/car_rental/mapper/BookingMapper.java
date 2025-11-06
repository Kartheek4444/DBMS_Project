package com.dbmsproject.car_rental.mapper;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.model.Booking;

public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto.BookingDtoBuilder builder = BookingDto.builder()
                .bookingId(booking.getBookingId())
                .customerId(booking.getCustomer() != null ? booking.getCustomer().getCustomerId() : null)
                .vehicleId(booking.getVehicle() != null ? booking.getVehicle().getVehicleId() : null)
                .pickupDate(booking.getPickupDate())
                .returnDate(booking.getReturnDate())
                .status(booking.getStatus())
                .depositAmount(booking.getDepositAmount())
                .bookingDate(booking.getBookingDate());

        // Populate customer name
        if (booking.getCustomer() != null) {
            builder.customerName(booking.getCustomer().getFirstName() + " " + booking.getCustomer().getLastName());
        }

        // Populate vehicle details
        if (booking.getVehicle() != null) {
            builder.vehicleMake(booking.getVehicle().getMake())
                    .vehicleModel(booking.getVehicle().getModel());
        }

        return builder.build();
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
