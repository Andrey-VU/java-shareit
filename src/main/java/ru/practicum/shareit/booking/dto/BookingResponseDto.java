package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private StatusOfBooking status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingResponseDto)) return false;
        BookingResponseDto that = (BookingResponseDto) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(item, that.item) && Objects.equals(booker, that.booker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, item, booker);
    }
}
