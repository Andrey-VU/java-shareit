package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingService;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto add(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody BookingDto bookingDto) {
        log.info("add booking: {} - Started", bookingDto);
        BookingDto bookingDtoFromRepo = bookingService.addNewBooking(userId, bookingDto);
        log.info("create booking: {} - Finished", bookingDtoFromRepo);
        return bookingDtoFromRepo;  /*
    Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
    а затем подтверждён владельцем вещи.
    Эндпоинт — POST /bookings. После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long bookingId,
                              @RequestBody BookingDto bookingDto) {
        log.info("Set status {} for booking id: {} by user id {}  - Started", bookingDto, bookingId, userId);
        BookingDto bookingDtoFromRepo = bookingService.approveBooking(userId, bookingId, bookingDto);
        log.info("Set status: {} - Finished", bookingDtoFromRepo.getStatus());
        return bookingDtoFromRepo;
         /*
    Подтверждение или отклонение запроса на бронирование. Может быть выполнено только владельцем вещи.
    Затем статус бронирования становится либо APPROVED, либо REJECTED.
    Эндпоинт — PATCH /bookings/{bookingId}?approved={approved},
    параметр approved может принимать значения true или false.
     */
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable long bookingId) {
        log.info("Search for booking id {} - Started", bookingId);
        BookingDto bookingDto = bookingService.getBooking(bookingId, userId);
        log.info("Booking {} was found", bookingDto);
        return bookingDto;
    /*
    Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором
    бронирования,
    либо владельцем вещи, к которой относится бронирование. Эндпоинт — GET /bookings/{bookingId}.
     */
    }

    @GetMapping
    public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(defaultValue = "ALL", required = false) StatusOfBooking status) {
        log.info("Search user's (id {}) bookings with status {} - Started", userId, status);
        List<BookingDto> bookingsOfUser = bookingService.getBookings(userId, status);
        log.info("{} bookings with status {} was found", bookingsOfUser.size(), status);
        return bookingsOfUser;
    /*
    Получение списка всех бронирований текущего пользователя. Эндпоинт — GET /bookings?state={state}.
     Параметр state необязательный и по умолчанию равен ALL (англ. «все»).
     Также он может принимать значения CURRENT (англ. «текущие»), **PAST** (англ. «завершённые»),
     FUTURE (англ. «будущие»), WAITING (англ. «ожидающие подтверждения»), REJECTED (англ. «отклонённые»).
    Бронирования должны возвращаться отсортированными по дате от более новых к более старым.
     */
    }


    @GetMapping("/owner")
    public List<BookingDto> getBookingsOfItemsThisUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(defaultValue = "ALL", required = false) StatusOfBooking status) {
        log.info("Search user's (id {}) bookings with status {} - Started", userId, status);
        List<BookingDto> bookingsOfUser = bookingService.getListOfBookingsOfItems(userId, status);
        log.info("{} bookings with status {} was found", bookingsOfUser.size(), status);
        return bookingsOfUser;/*
    Получение списка бронирований для всех вещей текущего пользователя.
    Эндпоинт — GET /bookings/owner?state={state}.
    Этот запрос имеет смысл для владельца хотя бы одной вещи.
    Работа параметра state аналогична его работе в предыдущем сценарии. */
    }



}