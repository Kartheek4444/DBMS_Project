package com.dbmsproject.car_rental.service;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto);
    BookingDto getBookingById(Long bookingId);
    BookingDto updateBooking(Long bookingId, BookingDto bookingDto);
    void cancelBooking(Long bookingId);
    List<BookingDto> getAllBookings();
    List<BookingDto> getBookingsByCustomerId(Long customerId);
    List<BookingDto> getBookingsByVehicleId(Long vehicleId);
    List<BookingDto> getBookingsByStatus(BookingStatus status);
    List<BookingDto> getBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<BookingDto> getActiveBookings();
    List<BookingDto> getUpcomingBookings();
    List<BookingDto> getPastBookings();
    BookingDto confirmBooking(Long bookingId);
    BookingDto completeBooking(Long bookingId);
    List<BookingDto> filterBookings(Long customerId, Long vehicleId, BookingStatus status, LocalDateTime startDate, LocalDateTime endDate);
}
