package ru.practicum.shareit.booking.model;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto addNewBooking(long id, BookingDto dto);

    BookingDto approveBooking(long userId, long bookingId, BookingDto dto);

    BookingDto getBooking(long bookingId, long userId);

    List<BookingDto> getBookings(long userId, StatusOfBooking status);

    List<BookingDto> getListOfBookingsOfItems(long userId, StatusOfBooking status);

}
