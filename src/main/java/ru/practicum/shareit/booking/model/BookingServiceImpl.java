package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
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
        ItemDto itemFromRepo = itemService.getItem(dto.getItemId());
        Item item = ItemMapper.makeItem(itemFromRepo, itemFromRepo.getOwnerId());
        if (!itemFromRepo.getAvailable()) {
            log.warn("Вещь id {}, недоступна для бронирования", dto.getItemId());
            throw new ValidationException("Item  is not available for booking");
        }
        dateValidate(dto);
        dto.setStatus(StatusOfBooking.WAITING);
        UserDto userFromRepo = userService.getUser(bookerId);
        User user = UserMapper.makeUserWithId(userFromRepo);
        Booking newBooking = bookingRepo.save(BookingMapper.requestDtoToEntity(dto, item, user));
        return BookingMapper.entityToResponseDto(newBooking);
    }

    @Override
    public BookingResponseDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        UserDto owner = userService.getUser(ownerId);
        if (approved == null) {
            log.warn("статус подтверждения не может быть пустым");
            throw new ValidationException("Approve validation error. Status is null");
        }
        BookingResponseDto bookingDtoFromRepo = getBooking(bookingId);
        if (bookingDtoFromRepo.getItem().getOwnerId() != ownerId) {
            log.warn("Подтверждение статуса бронирования доступно только владельцу вещи");
            throw new ValidationException("Access error. Only Owner can approve booking");
        }

        Booking bookingFromRepo = BookingMapper.responseDtoToEntity(bookingDtoFromRepo);
           bookingFromRepo.setStatus(approved.equals(true) ? StatusOfBooking.APPROVED
                : StatusOfBooking.REJECTED);

        Booking updateBooking = bookingRepo.save(bookingFromRepo);
        return BookingMapper.entityToResponseDto(updateBooking);
    }

    @Override
    public BookingResponseDto getBooking(Long bookingId) {

        Booking bookingFromRepo = bookingRepo.findById(bookingId).orElseThrow( () ->
                new BookingNotFoundException("Бронирование id "  + bookingId + " не найдено") );
        ItemDto myItem = itemService.getItem(bookingFromRepo.getItem().getId());
        UserDto myBooker = userService.getUser(bookingFromRepo.getBooker().getId());
        return BookingMapper.entityToResponseDto(bookingFromRepo);
    }

    @Override
    public List<BookingResponseDto> getBookings(Long bookerId, String status) {
        UserDto booker = userService.getUser(bookerId);
        List<BookingResponseDto> responseDtoList = new ArrayList<>();
        List<Booking> responseBookingList = new ArrayList<>();

        if (status.equals("All")) {
            responseBookingList = bookingRepo.findByBookerOrderByStartDesc(bookerId);
        }
        else if (status.equals("FUTURE")) {
            responseBookingList =
                    bookingRepo.findByBookerAndStartIsAfterOrderByStartDesc(bookerId, LocalDateTime.now());
        } else if (status.equals("CURRENT")) {
            responseBookingList =
                    bookingRepo.findByBookerAndEndIsAfterOrderByStartDesc(bookerId, LocalDateTime.now());
        } else if (status.equals("PAST")){
            responseBookingList =
                    bookingRepo.findByBookerAndEndIsBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
        }
//        else if (status.equals("WAITING")) {
//            responseBookingList =
//                    bookingRepo.findByBookerAndStatusIsWaitingOrderByStartDesc(bookerId);
//        } else if (status.equals("REJECTED")) {
//            responseBookingList =
//                    bookingRepo.findByBookerAndStatusIsRejectedOrderByStartDesc(bookerId);
//        }

        responseDtoList = responseBookingList.stream()
                .map(booking -> BookingMapper.entityToResponseDto(booking))
                .collect(Collectors.toList());

        return responseDtoList;
    }

    @Override
    public List<BookingResponseDto> getListOfBookingsOfItems(Long userId, StatusOfBooking status) {
        return null;
    }

    private void dateValidate(BookingRequestDto dto) {
        if (dto.getStart().isBefore(LocalDateTime.now())) {
            log.warn("Время начала бронирования не может быть в прошлом");
            throw new ValidationException("StartTime can't be from the past");
        };
        if (dto.getEnd().isBefore(dto.getStart()) || dto.getEnd().equals(dto.getStart())) {
            log.warn("Окончание бронирования должно быть позже начала бронирования");
            throw new ValidationException("EndTime can be later then StartDate");
        }
    }
}
