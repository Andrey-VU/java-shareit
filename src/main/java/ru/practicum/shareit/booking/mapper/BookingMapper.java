package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingMapper {
    Booking dtoToEntity(BookingDto dto);

    Booking updateEntity(Booking booking, BookingDto dto);

    BookingDto entityToDto(Booking entity);
}
