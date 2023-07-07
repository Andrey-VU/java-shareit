package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingForItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;

@ToString
@Setter
@Getter
@EqualsAndHashCode
public class ItemDtoGateway {
    private Long id;
    @NotBlank(message = "Item's name can't be null!")
    private String name;
    @NotBlank(message = "Item's description can't be null!")
    private String description;
    @NotNull(message = "Available-status of item can't be null!")
    private Boolean available;
    private Long ownerId;
    private Long requestId;

    private List<CommentDto> comments;

    private BookingForItemDto lastBooking;
    private BookingForItemDto nextBooking;

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof ItemDtoGateway)) return false;
//        ItemDtoGateway itemDto = (ItemDtoGateway) o;
//        return Objects.equals(name, itemDto.name) && Objects.equals(description, itemDto.description) && Objects.equals(ownerId, itemDto.ownerId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, description, ownerId);
//    }
}

