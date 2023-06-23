package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

import java.util.List;

@Data
@ToString
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;


    private List<CommentDto> comments;

    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;
}
