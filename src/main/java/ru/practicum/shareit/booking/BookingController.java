package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingService;
import ru.practicum.shareit.booking.model.StatusOfBooking;

import javax.validation.Valid;
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
    public BookingResponseDto add(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                  @Valid @RequestBody BookingRequestDto bookingDto) {
        log.info("Add new booking: {} - Started", bookingDto);
        BookingResponseDto bookingDtoFromRepo = bookingService.addNewBooking(bookerId, bookingDto);
        log.info("Create booking: {} - Finished", bookingDtoFromRepo);
        return bookingDtoFromRepo;

        /*
    Добавление нового запроса на бронирование. Запрос может быть создан любым пользователем,
    а затем подтверждён владельцем вещи.
    Эндпоинт — POST /bookings. После создания запрос находится в статусе WAITING — «ожидает подтверждения».
     */
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @PathVariable Long bookingId,
                                      @RequestParam Boolean approved) {
        log.info("Set status {} for booking id: {} by user id {}  - Started", approved, bookingId, ownerId);
        BookingResponseDto bookingDtoFromRepo = bookingService.approveBooking(ownerId, bookingId, approved);
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
    public BookingResponseDto getBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId) {
        log.info("Search for booking id {} - Started", bookingId);
        BookingResponseDto bookingResponseDto = bookingService.getBooking(bookingId, userId);
        log.info("Booking {} was found", bookingResponseDto);
        return bookingResponseDto;
    /*
    Получение данных о конкретном бронировании (включая его статус). Может быть выполнено либо автором
    бронирования,
    либо владельцем вещи, к которой относится бронирование. Эндпоинт — GET /bookings/{bookingId}.
     */
    }

    @GetMapping
    public List<BookingResponseDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        log.info("Search user's (id {}) {} bookings - Started", bookerId, state);
        List<BookingResponseDto> bookingsOfUser = bookingService.getBookings(bookerId, state);
        log.info("{} {} bookings was found", bookingsOfUser.size(), state);
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
    public List<BookingResponseDto> getBookingsOfOwnersItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                    @RequestParam(defaultValue = "ALL") String state) {
        log.info("Search {} bookings of owner's (id {}) items - Started", state, ownerId);
        List<BookingResponseDto> bookingsOfOwnerItems = bookingService.getListOfBookingsOfOwnersItems(ownerId,state);
        log.info("{} {} bookings was found", state, bookingsOfOwnerItems.size());
        return bookingsOfOwnerItems;
        /*
    Получение списка бронирований для всех вещей текущего пользователя.
    Эндпоинт — GET /bookings/owner?state={state}.
    Этот запрос имеет смысл для владельца хотя бы одной вещи.
    Работа параметра state аналогична его работе в предыдущем сценарии. */
    }



}