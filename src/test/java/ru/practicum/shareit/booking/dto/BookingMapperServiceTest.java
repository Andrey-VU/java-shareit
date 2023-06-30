package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateForBooking;
import ru.practicum.shareit.booking.model.StatusOfBooking;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.BookingNotFoundException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperServiceTest {
    private final Long id1 = 1L;
    private final Long id2 = 2L;
    private LocalDateTime start = LocalDateTime.now();
    private LocalDateTime end = start.plusDays(1);
    private User userOwner;
    private User userBooker;
    private Item item;
    private Booking bookingForSave;
    private Booking newBooking;
    private BookingRequestDto newBookingRequestDto;
    private BookingResponseDto bookingResponseDtoFromRepo;
    private Booking approvedBooking;
    private Booking rejectedBooking;

    @Mock
    private ItemService itemService;
    @Mock
    private UserService userService;
    @Mock
    private BookingRepository bookingRepo;

    @InjectMocks
    private BookingMapperService bookingMapperService;

    @BeforeEach
    void setUp() {
        newBookingRequestDto = BookingRequestDto.builder()
                .itemId(id1)
                .start(start)
                .end(end)
                .build();

        userOwner = User.builder()
                .id(id1)
                .email("o@o.ru")
                .name("OwnerUser")
                .build();

        userBooker = User.builder()
                .id(id2)
                .email("b@b.ru")
                .name("BookerUser")
                .build();

        item = Item.builder()
                .description("item description")
                .isAvailable(true)
                .id(id1)
                .name("test item")
                .owner(userOwner)
                .build();

        bookingForSave = BookingMapper.requestDtoToEntity(newBookingRequestDto, item, userOwner).orElseThrow();

        newBooking = Booking.builder()
                .booker(userBooker)
                .id(id1)
                .status(StatusOfBooking.WAITING)
                .start(start)
                .end(end)
                .item(item)
                .build();

        approvedBooking = Booking.builder()
                .booker(userBooker)
                .id(id1)
                .status(StatusOfBooking.APPROVED)
                .start(start)
                .end(end)
                .item(item)
                .build();

        rejectedBooking = Booking.builder()
                .booker(userBooker)
                .id(id1)
                .status(StatusOfBooking.REJECTED)
                .start(start)
                .end(end)
                .item(item)
                .build();


        bookingResponseDtoFromRepo = BookingMapper.entityToResponseDto(newBooking).orElseThrow();
    }


    @Test
    void addStatusToBooking_whenApprovedIsTrue_thenReturnEntityWithStatusApproved() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(newBooking));
        assertEquals(approvedBooking, bookingMapperService.addStatusToBooking(1L, 1L, true));
    }

    @Test
    void addStatusToBooking_whenApprovedIsFalse_thenReturnEntityWithStatusRejected() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(newBooking));
        assertEquals(rejectedBooking, bookingMapperService.addStatusToBooking(1L, 1L, false));
    }

    @Test
    void addStatusToBooking_whenSecondaryApproved_thenThrowValidationException() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(approvedBooking));
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.addStatusToBooking(1L, 1L, true));
        validationException.getMessage();
    }

    @Test
    void addStatusToBooking_whenApprovedIsNull_thenThrowValidationException() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(newBooking));
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.addStatusToBooking(1L, 1L, null));
        validationException.getMessage();
    }

    @Test
    void addStatusToBooking_whenApprovedIsNull2_thenThrowValidationException() {
        when(bookingRepo.findById(1L)).thenThrow(ValidationException.class);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.addStatusToBooking(1L, 1L, null));
        validationException.getMessage();
    }

    @Test
    void addStatusToBooking_whenNotOwnerTryToApprove_thenThrowBookingNotFoundException() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.ofNullable(newBooking));
        BookingNotFoundException bookingNotFoundException = assertThrows(BookingNotFoundException.class,
                () -> bookingMapperService.addStatusToBooking(2L, 1L, true));
        bookingNotFoundException.getMessage();
    }

    @Test
    void bookingRequestPrepareForAdd_whenAvailableFalse_thenReturnValidationException() {
        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
        itemDto.setAvailable(false);
        when(itemService.getItem(1L, id2)).thenReturn(itemDto);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(id2, newBookingRequestDto));
        validationException.getMessage();
    }

    @Test
    void bookingRequestPrepareForAdd_whenStartDateNotValid_thenReturnValidationException() {
        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
        when(itemService.getItem(1L, id2)).thenReturn(itemDto);

        BookingRequestDto bookingRequestDtoStartAfterEnd = BookingRequestDto.builder()
                .itemId(id1)
                .start(start)
                .end(start.minusDays(1))
                .build();

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(id2, bookingRequestDtoStartAfterEnd));
        validationException.getMessage();
    }

    @Test
    void bookingRequestPrepareForAdd_whenStartEqualsEnd_thenReturnValidationException() {
        BookingRequestDto bookingRequestDtoWithStartAndEndEquals = BookingRequestDto.builder()
                .itemId(id1)
                .start(start)
                .end(start)
                .build();

        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
        when(itemService.getItem(1L, id2)).thenReturn(itemDto);
        newBookingRequestDto.setStart(end);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(id2, bookingRequestDtoWithStartAndEndEquals));
        validationException.getMessage();
    }

    @Test
    void accessVerification() {
    }

    @Test
    void prepareResponseDtoList() {
    }


//    @Test
//    void prepareResponseDtoListForOwner_whenRequestFromOwner() {
//        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
//        when(userService.getUser(id1)).thenReturn(UserMapper.makeDto(userOwner).orElseThrow());
//        when(itemService.getItems(id1)).thenReturn(List.of(itemDto));
//    }

    @Test
    void prepareResponseDtoListForOwner_whenRequestNotFromOwner_thenThrowUserNotFoundException() {
        when(userService.getUser(id2)).thenReturn(UserMapper.makeDto(userBooker).orElseThrow());
        when(itemService.getItems(id2)).thenReturn(new ArrayList<>());
        ItemNotFoundException itemNotFoundException = assertThrows(ItemNotFoundException.class,
                () -> bookingMapperService.prepareResponseDtoListForOwner(id2, StateForBooking.ALL, 0, 1));
        itemNotFoundException.getMessage();
    }
}