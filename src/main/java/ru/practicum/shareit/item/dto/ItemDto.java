package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

@Data
@ToString
public class ItemDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;

    private BookingResponseDto lastBooking;
    private BookingResponseDto nextBooking;
}
