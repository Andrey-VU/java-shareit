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
    private final Long ownerId1 = 1L;
    private final Long bookerId2 = 2L;
    private LocalDateTime start = LocalDateTime.now().plusDays(1);
    private LocalDateTime end = start.plusDays(2);
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
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        userOwner = User.builder()
                .id(ownerId1)
                .email("o@o.ru")
                .name("OwnerUser")
                .build();

        userBooker = User.builder()
                .id(bookerId2)
                .email("b@b.ru")
                .name("BookerUser")
                .build();

        item = Item.builder()
                .description("item description")
                .isAvailable(true)
                .id(1L)
                .name("test item")
                .owner(userOwner)
                .build();

        bookingForSave = BookingMapper.requestDtoToEntity(newBookingRequestDto, item, userOwner).orElseThrow();

        newBooking = Booking.builder()
                .booker(userBooker)
                .id(1L)
                .status(StatusOfBooking.WAITING)
                .start(start)
                .end(end)
                .item(item)
                .build();

        approvedBooking = Booking.builder()
                .booker(userBooker)
                .id(1L)
                .status(StatusOfBooking.APPROVED)
                .start(start)
                .end(end)
                .item(item)
                .build();

        rejectedBooking = Booking.builder()
                .booker(userBooker)
                .id(1L)
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
        when(itemService.getItem(1L, bookerId2)).thenReturn(itemDto);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(bookerId2, newBookingRequestDto));
        validationException.getMessage();
    }

    @Test
    void bookingRequestPrepareForAdd_whenStartDateNotValid_thenReturnValidationException() {
        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
        when(itemService.getItem(1L, bookerId2)).thenReturn(itemDto);

        BookingRequestDto bookingRequestDtoStartAfterEnd = BookingRequestDto.builder()
                .itemId(ownerId1)
                .start(start)
                .end(start.minusDays(1))
                .build();

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(bookerId2, bookingRequestDtoStartAfterEnd));
        validationException.getMessage();
    }

    @Test
    void bookingRequestPrepareForAdd_whenStartDateIsInThePast_thenReturnValidationException() {
        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
        when(itemService.getItem(1L, bookerId2)).thenReturn(itemDto);

        BookingRequestDto bookingRequestDtoStartAfterEnd = BookingRequestDto.builder()
                .itemId(ownerId1)
                .start(start.minusDays(5))
                .end(end)
                .build();

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(bookerId2, bookingRequestDtoStartAfterEnd));
        validationException.getMessage();
    }

    @Test
    void bookingRequestPrepareForAdd_whenStartEqualsEnd_thenReturnValidationException() {
        BookingRequestDto bookingRequestDtoWithStartAndEndEquals = BookingRequestDto.builder()
                .itemId(ownerId1)
                .start(start)
                .end(start)
                .build();

        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
        when(itemService.getItem(1L, bookerId2)).thenReturn(itemDto);
        newBookingRequestDto.setStart(end);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(bookerId2, bookingRequestDtoWithStartAndEndEquals));
        validationException.getMessage();
    }

    @Test
    void bookingRequestPrepareForAdd_whenRequestValid_thenReturnBooking() {
        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();

        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .itemId(ownerId1)
                .start(start)
                .end(end)
                .build();

        when(itemService.getItem(1L, bookerId2)).thenReturn(itemDto);
        when(userService.getUser(bookerId2)).thenReturn(UserMapper.makeDto(userBooker).get());
        when(userService.getUser(ownerId1)).thenReturn(UserMapper.makeDto(userOwner).get());

        assertEquals(newBooking, bookingMapperService.bookingRequestPrepareForAdd(bookerId2, bookingRequestDto));
    }

    @Test
    void bookingRequestPrepareForAdd_whenUserEqualsOwner_thenThrowBookingNotFoundException() {
        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();

        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .itemId(ownerId1)
                .start(start)
                .end(end)
                .build();

        when(itemService.getItem(1L, ownerId1)).thenReturn(itemDto);
        when(userService.getUser(ownerId1)).thenReturn(UserMapper.makeDto(userOwner).get());
        when(userService.getUser(ownerId1)).thenReturn(UserMapper.makeDto(userOwner).get());

        BookingNotFoundException ex = assertThrows(BookingNotFoundException.class,
                () -> bookingMapperService.bookingRequestPrepareForAdd(ownerId1, bookingRequestDto));
        ex.getMessage();
    }


    @Test
    void accessVerification_whenCorrect_thenReturnTrue() {
        assertEquals(true,
                bookingMapperService.accessVerification(newBooking, bookerId2));
    }

    @Test
    void accessVerification_whenUserIsNotBooker_thenThrowBookingNotFoundException() {
        BookingNotFoundException ex = assertThrows(BookingNotFoundException.class,
                () -> bookingMapperService.accessVerification(newBooking, 4L));
    }

    @Test
    void prepareResponseDtoList_whenResponseCorrectAndStateAll_thenReturnListDto() {
//        PageRequest pageRequest = PageRequest.of(0, 1);
//        when(userService.getUser(bookerId2)).thenReturn(UserMapper.makeDto(userBooker).get());

    }


//    @Test
//    void prepareResponseDtoListForOwner_whenRequestFromOwner() {
//        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
//        when(userService.getUser(id1)).thenReturn(UserMapper.makeDto(userOwner).orElseThrow());
//        when(itemService.getItems(id1)).thenReturn(List.of(itemDto));
//    }

    @Test
    void prepareResponseDtoListForOwner_whenRequestNotFromOwner_thenThrowUserNotFoundException() {
        when(userService.getUser(bookerId2)).thenReturn(UserMapper.makeDto(userBooker).orElseThrow());
        when(itemService.getItems(bookerId2)).thenReturn(new ArrayList<>());
        ItemNotFoundException itemNotFoundException = assertThrows(ItemNotFoundException.class,
                () -> bookingMapperService.prepareResponseDtoListForOwner(bookerId2, StateForBooking.ALL, 0, 1));
        itemNotFoundException.getMessage();
    }
}