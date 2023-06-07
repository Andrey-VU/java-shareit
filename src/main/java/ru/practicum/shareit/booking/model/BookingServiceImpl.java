package ru.practicum.shareit.booking.model;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Service("BookingService")
public class BookingServiceImpl implements BookingService {
    @Override
    public BookingDto addNewBooking(long id, BookingDto dto) {
        return null;
    }

    @Override
    public BookingDto approveBooking(long userId, long bookingId, BookingDto dto) {
        return null;
    }

    @Override
    public BookingDto getBooking(long bookingId, long userId) {
        return null;
    }

    @Override
    public List<BookingDto> getBookings(long userId, StatusOfBooking status) {
        return null;
    }

    @Override
    public List<BookingDto> getListOfBookingsOfItems(long userId, StatusOfBooking status) {
        return null;
    }
}
