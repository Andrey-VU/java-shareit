package ru.practicum.shareit.booking.mapper;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingForItemDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@Slf4j
public final class BookingMapper {
    public static Booking requestDtoToEntity(BookingRequestDto dto, Item item, User booker) {
        Booking booking = new Booking();
        if (dto == null) {
            return null;
        } else {
            booking.setStart(dto.getStart());
            booking.setEnd(dto.getEnd());
            booking.setItem(item);
            booking.setStatus(dto.getStatus());
            booking.setBooker(booker);
        }
        return booking;
    }

    public static Booking updateEntity(Booking booking, BookingRequestDto dto) {
        if (dto.getStatus() != null) {
            booking.setStatus(dto.getStatus());
        }
        if (dto.getStart() != null) {
            booking.setStart(dto.getStart());
        }
        if (dto.getEnd() != null) {
            booking.setEnd(dto.getEnd());
        }
        return booking;
    }

    public static BookingResponseDto entityToResponseDto(Booking entity) {
        BookingResponseDto dto = new BookingResponseDto();
        if (entity == null) {
            return null;
        } else {
            dto.setStart(entity.getStart());
            dto.setEnd(entity.getEnd());
            dto.setItem(ItemMapper.makeDtoFromItem(entity.getItem()));
            dto.setStatus(entity.getStatus());
            dto.setId(entity.getId());
            dto.setBooker(UserMapper.makeDto(entity.getBooker()));
        }
        return dto;
    }

    public static Booking responseDtoToEntity(BookingResponseDto dto, User owner) {
        Booking booking = new Booking();
        if (dto == null) {
            return null;
        } else {
            booking.setId(dto.getId());
            booking.setStart(dto.getStart());
            booking.setEnd(dto.getEnd());
            booking.setItem(ItemMapper.makeItem(dto.getItem(), owner));
            booking.setStatus(dto.getStatus());
            booking.setBooker(UserMapper.makeUserWithId(dto.getBooker()));
        }
        return booking;
    }

    public static BookingForItemDto entityToBookingForItemDto(Booking booking) {
        BookingForItemDto dto = new BookingForItemDto();
        if (booking == null) {
            return null;
        } else {
            dto.setStart(booking.getStart());
            dto.setEnd(booking.getEnd());
            dto.setItem(ItemMapper.makeDtoFromItem(booking.getItem()));
            dto.setStatus(booking.getStatus());
            dto.setId(booking.getId());
            dto.setBookerId(booking.getBooker().getId());
        }
        return dto;
    }
}
