package com.dbmsproject.car_rental.service.impl;

import com.dbmsproject.car_rental.dto.BookingDto;
import com.dbmsproject.car_rental.exception.ResourceNotFoundException;
import com.dbmsproject.car_rental.model.Booking;
import com.dbmsproject.car_rental.model.BookingStatus;
import com.dbmsproject.car_rental.model.Customer;
import com.dbmsproject.car_rental.model.Vehicle;
import com.dbmsproject.car_rental.repository.BookingRepository;
import com.dbmsproject.car_rental.repository.CustomerRepository;
import com.dbmsproject.car_rental.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Customer testCustomer;
    private Vehicle testVehicle;
    private BookingDto bookingDto;
    private Booking booking;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        testCustomer = Customer.builder()
                .customerId(10L)
                .firstName("Alice")
                .lastName("Smith")
                .email("alice@example.com")
                .phoneNumber("9998887777")
                .build();

        testVehicle = Vehicle.builder()
                .vehicleId(20L)
                .make("Toyota")
                .model("Camry")
                .year(2022)
                .pricePerDay(new BigDecimal("50.00"))
                .build();

        bookingDto = BookingDto.builder()
                .customerId(10L)
                .vehicleId(20L)
                .pickupDate(now.plusDays(1))
                .returnDate(now.plusDays(3))
                .depositAmount(new BigDecimal("150.00"))
                .status(BookingStatus.PENDING)
                .build();

        booking = Booking.builder()
                .bookingId(100L)
                .customer(testCustomer)
                .vehicle(testVehicle)
                .pickupDate(now.plusDays(1))
                .returnDate(now.plusDays(3))
                .depositAmount(new BigDecimal("150.00"))
                .status(BookingStatus.PENDING)
                .build();
    }

    @Test
    void createBooking_Success() {
        when(customerRepository.findById(10L)).thenReturn(Optional.of(testCustomer));
        when(vehicleRepository.findById(20L)).thenReturn(Optional.of(testVehicle));
        when(bookingRepository.existsOverlappingBooking(eq(20L), any(), any(), eq(null))).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.createBooking(bookingDto);

        assertNotNull(result);
        assertEquals(100L, result.getBookingId());
        assertEquals(BookingStatus.PENDING, result.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_MissingDates_ThrowsException() {
        bookingDto.setPickupDate(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(bookingDto)
        );

        assertTrue(exception.getMessage().contains("required"));
    }

    @Test
    void createBooking_InvalidDateRange_ThrowsException() {
        bookingDto.setReturnDate(now.minusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(bookingDto)
        );

        assertTrue(exception.getMessage().contains("must be after pickup date"));
    }

    @Test
    void createBooking_Overlapping_ThrowsException() {
        when(customerRepository.findById(10L)).thenReturn(Optional.of(testCustomer));
        when(vehicleRepository.findById(20L)).thenReturn(Optional.of(testVehicle));
        when(bookingRepository.existsOverlappingBooking(eq(20L), any(), any(), eq(null))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.createBooking(bookingDto)
        );

        assertTrue(exception.getMessage().contains("already booked"));
    }

    @Test
    void getBookingById_Success() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        BookingDto result = bookingService.getBookingById(100L);

        assertNotNull(result);
        assertEquals(100L, result.getBookingId());
    }

    @Test
    void getBookingById_NotFound_ThrowsException() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                bookingService.getBookingById(999L)
        );
    }

    @Test
    void updateBooking_Success() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto updateDto = BookingDto.builder()
                .pickupDate(now.plusDays(2))
                .returnDate(now.plusDays(4))
                .status(BookingStatus.CONFIRMED)
                .build();

        BookingDto result = bookingService.updateBooking(100L, updateDto);

        assertNotNull(result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void confirmBooking_Success() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        booking.setStatus(BookingStatus.CONFIRMED);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.confirmBooking(100L);

        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void cancelBooking_Success() {
        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(100L);

        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        verify(bookingRepository).save(booking);
    }

    @Test
    void filterBookings_Success() {
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        List<BookingDto> filtered = bookingService.filterBookings(10L, 20L, BookingStatus.PENDING, null, null);

        assertEquals(1, filtered.size());
        assertEquals(100L, filtered.get(0).getBookingId());
    }
}
