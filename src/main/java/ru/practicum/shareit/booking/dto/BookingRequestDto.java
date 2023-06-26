package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    @NotNull(message = "Время начала бронирования не может быть пустым")
    private LocalDateTime start;

    @NotNull(message = "Время завершения бронирования не может быть пустым")
    private LocalDateTime end;

    @NotNull(message = "Бронирование невозможно без указания Item Id")
    private Long itemId;

    private StatusOfBooking status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingRequestDto)) return false;
        BookingRequestDto that = (BookingRequestDto) o;
        return Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, itemId);
    }
}
