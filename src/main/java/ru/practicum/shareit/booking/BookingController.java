package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDto add(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                  @Valid @RequestBody BookingRequestDto bookingDto) {
        log.info("Add new booking: {} - Started", bookingDto);
        BookingResponseDto bookingDtoFromRepo = bookingService.addNewBooking(bookerId, bookingDto);
        log.info("Create booking: {} - Finished", bookingDtoFromRepo);
        return bookingDtoFromRepo;
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @PathVariable Long bookingId,
                                      @RequestParam Boolean approved) {
        log.info("Set status {} for booking id: {} by user id {}  - Started", approved, bookingId, ownerId);
        BookingResponseDto bookingDtoFromRepo = bookingService.approveBooking(ownerId, bookingId, approved);
        log.info("Set status: {} - Finished", bookingDtoFromRepo.getStatus());
        return bookingDtoFromRepo;
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long bookingId) {
        log.info("Search for booking id {} - Started", bookingId);
        BookingResponseDto bookingResponseDto = bookingService.getBooking(bookingId, userId);
        log.info("Booking {} was found", bookingResponseDto);
        return bookingResponseDto;
    }

    @GetMapping
    public List<BookingResponseDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @Valid @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                @Valid @RequestParam(required = false, defaultValue = "20") @Min(1) Integer size) {
        log.info("Search user's (id {}) {} bookings - Started", bookerId, state);
        List<BookingResponseDto> bookingsOfUser = bookingService.getBookings(bookerId, state, from, size);
        log.info("{} {} bookings was found", bookingsOfUser.size(), state);
        return bookingsOfUser;
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getBookingsOfOwnersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                             @RequestParam(defaultValue = "ALL") String state,
                                                @Valid @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                @Valid @RequestParam(required = false, defaultValue = "20") @Min(1) Integer size) {
        log.info("Search {} bookings of owner's (id {}) items - Started", state, ownerId);
        List<BookingResponseDto> bookingsOfOwnerItems =
                bookingService.getListOfBookingsOfOwnersItems(ownerId, state, from, size);
        log.info("{} {} bookings was found", state, bookingsOfOwnerItems.size());
        return bookingsOfOwnerItems;
    }
}