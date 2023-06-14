package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service("BookingService")
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepo;
    UserServiceImpl userService;
    ItemServiceImpl itemService;

    @Override
    public BookingResponseDto addNewBooking(Long bookerId, BookingRequestDto dto) {
        ItemDto itemDtoFromRepo = itemService.getItem(dto.getItemId(), bookerId);
        User owner = UserMapper.makeUserWithId(userService.getUser(itemDtoFromRepo.getOwnerId()));
        Item item = ItemMapper.makeItem(itemDtoFromRepo, owner);
        if (!itemDtoFromRepo.getAvailable()) {
            log.warn("Вещь id {}, недоступна для бронирования", dto.getItemId());
            throw new ValidationException("Item  is not available for booking");
        }
        dateValidate(dto);
        dto.setStatus(StatusOfBooking.WAITING);
        UserDto userFromRepo = userService.getUser(bookerId);

        if (userFromRepo.getId() == item.getOwner().getId()) {
            log.warn("Внимание! Попытка создать бронирование собственной вещи!");
            throw new BookingNotFoundException("Owner of item can't book it!");
        }

        User user = UserMapper.makeUserWithId(userFromRepo);
        Booking newBooking = bookingRepo.save(BookingMapper.requestDtoToEntity(dto, item, user));
        return BookingMapper.entityToResponseDto(newBooking);
    }

    @Override
    public BookingResponseDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        User owner = UserMapper.makeUserWithId(userService.getUser(ownerId));
        if (!getBooking(bookingId, ownerId).getStatus().equals(StatusOfBooking.WAITING)) {
            log.warn("Статус бронирования уже был установлен");
            throw new ValidationException("Secondary approval is prohibited!");
        }

        if (approved == null) {
            log.warn("статус подтверждения не может быть пустым");
            throw new ValidationException("Approve validation error. Status is null");
        }
        BookingResponseDto bookingDtoFromRepo = getBooking(bookingId, ownerId);
        if (bookingDtoFromRepo.getItem().getOwnerId() != ownerId) {
            log.warn("Подтверждение статуса бронирования доступно только владельцу вещи");
            throw new BookingNotFoundException("Access error. Only Owner can approve booking");
        }

        Booking bookingFromRepo = BookingMapper.responseDtoToEntity(bookingDtoFromRepo, owner);
        bookingFromRepo.setStatus(approved.equals(true) ? StatusOfBooking.APPROVED
                : StatusOfBooking.REJECTED);

        Booking updateBooking = bookingRepo.save(bookingFromRepo);
        return BookingMapper.entityToResponseDto(updateBooking);
    }

    @Override
    public BookingResponseDto getBooking(Long bookingId, Long userId) {

        Booking bookingFromRepo = bookingRepo.findById(bookingId).orElseThrow(() ->
                new BookingNotFoundException("Бронирование id " + bookingId + " не найдено"));

        if (!(bookingFromRepo.getBooker().getId().equals(userId)
                || bookingFromRepo.getItem().getOwner().getId() == userId)) {
            log.warn("Просмотр бронирования доступен только арендатору или владельцу вещи");
            throw new BookingNotFoundException("Access error. Only for Owner or Booker");
        }

        return BookingMapper.entityToResponseDto(bookingFromRepo);
    }

    @Override
    public List<BookingResponseDto> getBookings(Long bookerId, String state) {
        userService.getUser(bookerId);
        List<BookingResponseDto> responseDtoList;
        List<Booking> responseBookingList = new ArrayList<>();

        if (state.equals("ALL")) {
            responseBookingList = bookingRepo.findByBookerIdOrderByStartDesc(bookerId);
        } else if (state.equals("FUTURE")) {
            responseBookingList =
                    bookingRepo.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
        } else if (state.equals("CURRENT")) {
            responseBookingList =
                    bookingRepo.findAllByBookerIdAndEndAfterOrderByStartDesc(bookerId, LocalDateTime.now());
        } else if (state.equals("PAST")) {
            responseBookingList =
                    bookingRepo.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
        } else if (state.equals("WAITING") || state.equals("REJECTED")) {
            responseBookingList =
                    bookingRepo.findAllByBookerIdOrderByStartDesc(bookerId);
            responseBookingList = responseBookingList.stream()
                    .filter(booking -> booking.getStatus().equals(StatusOfBooking.valueOf(state)))
                    .collect(Collectors.toList());
        } else {
            log.warn("Статус запроса {} не поддерживается", state);
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }

        responseDtoList = responseBookingList.stream()
                .map(booking -> BookingMapper.entityToResponseDto(booking))
                .collect(Collectors.toList());
        return responseDtoList;
    }

    @Override
    public List<BookingResponseDto> getListOfBookingsOfOwnersItems(Long ownerId, String state) {
        userService.getUser(ownerId);
        if (itemService.getItems(ownerId).size() == 0) {
            log.warn("Пользователь {} не владеет вещами", ownerId);
            throw new ItemNotFoundException("Items of user is not found!");
        }

        List<Booking> bookingsOfOwnersItems = new ArrayList<>();

        switch (state) {
            case "ALL":
                bookingsOfOwnersItems = bookingRepo.findAllByItemOwnerIdOrderByStartDesc(ownerId);
                break;
            case "FUTURE":
                bookingsOfOwnersItems =
                        bookingRepo.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "CURRENT":
                bookingsOfOwnersItems =
                        bookingRepo.findAllByItemOwnerIdAndEndAfterOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "PAST":
                bookingsOfOwnersItems =
                        bookingRepo.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
                break;
            case "WAITING":
            case "REJECTED":
                bookingsOfOwnersItems =
                        bookingRepo.findAllByItemOwnerIdOrderByStartDesc(ownerId);
                bookingsOfOwnersItems = bookingsOfOwnersItems.stream()
                        .filter(booking -> booking.getStatus().equals(StatusOfBooking.valueOf(state)))
                        .collect(Collectors.toList());

                break;
            default:
                log.warn("Статус запроса {} не поддерживается", state);
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }

        return bookingsOfOwnersItems.stream()
                .map(booking -> BookingMapper.entityToResponseDto(booking))
                .collect(Collectors.toList());
    }

    private void dateValidate(BookingRequestDto dto) {
        if (dto.getStart().isBefore(LocalDateTime.now())) {
            log.warn("Время начала бронирования не может быть в прошлом");
            throw new ValidationException("StartTime can't be from the past");
        }
        ;
        if (dto.getEnd().isBefore(dto.getStart()) || dto.getEnd().equals(dto.getStart())) {
            log.warn("Окончание бронирования должно быть позже начала бронирования");
            throw new ValidationException("EndTime can be later then StartDate");
        }
    }
}
