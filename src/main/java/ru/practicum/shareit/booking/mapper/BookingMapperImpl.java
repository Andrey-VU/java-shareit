package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapperImpl implements BookingMapper{
    @Override
    public Booking dtoToEntity(BookingDto dto) {
        Booking booking = new Booking();
        if (dto == null) {
            return null;
        } else {
            booking.setStartDate(dto.getStartDate());
            booking.setEndDate(dto.getEndDate());
            booking.setItemId(dto.getItemId());
            booking.setStatus(dto.getStatus());
        }
    return booking;
    }

    @Override
    public Booking updateEntity(Booking booking, BookingDto dto) {
        return null;
    }

    @Override
    public BookingDto entityToDto(Booking entity) {
        return null;
    }
}
