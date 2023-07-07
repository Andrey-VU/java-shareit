package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookingRequestDto {

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    private StatusOfBooking status;
}
