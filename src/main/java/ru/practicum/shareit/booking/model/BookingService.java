package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;

public interface BookingService {

    BookingResponseDto addNewBooking(Long bookerId, BookingRequestDto dto);

    BookingResponseDto approveBooking(Long ownerId, Long bookingId, Boolean approved);

    BookingResponseDto getBooking(Long bookingId);

    List<BookingResponseDto> getBookings(Long userId, String status);

    List<BookingResponseDto> getListOfBookingsOfOwnersItems(Long ownerId, String status);
}
