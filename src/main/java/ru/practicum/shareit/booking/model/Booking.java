package ru.practicum.shareit.booking.model;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Getter @Setter
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(name = "start_date")
    private Instant startDate = Instant.now();

    @Column(name = "end_date")
    private Instant endDate;

    @NotBlank
    @Column(name = "item_id")
    private long itemId;

//    @NotBlank
//    @Column(name = "requester_id")
//    private long requesterId;

    @NotBlank
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusOfBooking status;
}