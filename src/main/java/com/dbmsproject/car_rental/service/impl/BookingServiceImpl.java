package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.mapper.BookingMapper;
import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.BookingStatus;
import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.repository.BookingRepository;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.repository.VehicleRepository;
import com.dbmsproject.car_rental.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        Customer customer = customerRepository.findById(bookingDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Vehicle vehicle = vehicleRepository.findById(bookingDto.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        Booking booking = Booking.builder()
                .customer(customer)
                .vehicle(vehicle)
                .pickupDate(bookingDto.getPickupDate())
                .returnDate(bookingDto.getReturnDate())
                .status(BookingStatus.PENDING)
                .depositAmount(bookingDto.getDepositAmount())
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDto getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return BookingMapper.toDto(booking);
    }

    @Override
    public BookingDto updateBooking(Long bookingId, BookingDto bookingDto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        booking.setPickupDate(bookingDto.getPickupDate());
        booking.setReturnDate(bookingDto.getReturnDate());
        booking.setDepositAmount(bookingDto.getDepositAmount());
        booking.setStatus(bookingDto.getStatus());

        Booking updatedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(updatedBooking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomer_CustomerId(customerId).stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByVehicleId(Long vehicleId) {
        return bookingRepository.findByVehicle_VehicleId(vehicleId).stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status).stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getBookingsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByPickupDateBetween(startDate, endDate).stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getActiveBookings() {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findByPickupDateBeforeAndReturnDateAfter(now, now).stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getUpcomingBookings() {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findByPickupDateAfter(now).stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getPastBookings() {
        LocalDateTime now = LocalDateTime.now();
        return bookingRepository.findByReturnDateBefore(now).stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        Booking confirmedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(confirmedBooking);
    }

    @Override
    public BookingDto completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setStatus(BookingStatus.COMPLETED);
        Booking completedBooking = bookingRepository.save(booking);
        return BookingMapper.toDto(completedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> filterBookings(Long customerId, Long vehicleId, BookingStatus status,
                                           LocalDateTime startDate, LocalDateTime endDate) {
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .filter(booking -> customerId == null || booking.getCustomer().getCustomerId().equals(customerId))
                .filter(booking -> vehicleId == null || booking.getVehicle().getVehicleId().equals(vehicleId))
                .filter(booking -> status == null || booking.getStatus().equals(status))
                .filter(booking -> startDate == null || !booking.getPickupDate().isBefore(startDate))
                .filter(booking -> endDate == null || !booking.getPickupDate().isAfter(endDate))
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }



}
