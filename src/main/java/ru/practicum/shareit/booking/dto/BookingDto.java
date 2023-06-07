package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.time.Instant;

@Getter @Setter
public class BookingDto {
    private long id;
    private Instant startDate;
    private Instant endDate;
    private long itemId;
//    private long requesterId;
    private StatusOfBooking status;
}
