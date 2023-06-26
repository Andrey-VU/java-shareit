package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingMapperService;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    BookingRequestDto newBookingRequestDto;
    final static Long ID_1 = 1L;
    final static Long ID_2 = 2L;
    private LocalDateTime start = LocalDateTime.now();
    private LocalDateTime end = start.plusDays(1);
    private User userOwner;
    private User userBooker;
    private Item item;
    private Booking bookingForSave;
    private Booking newBooking;

    @Mock
    BookingRepository bookingRepo;
    @Mock
    BookingMapperService bookingMapperService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        newBookingRequestDto = BookingRequestDto.builder()
                .itemId(ID_1)
                .start(start)
                .end(end)
                .build();

        userOwner = User.builder()
                .id(ID_1)
                .email("o@o.ru")
                .name("OwnerUser")
                .build();

        userBooker = User.builder()
                .id(ID_2)
                .email("b@b.ru")
                .name("BookerUser")
                .build();

        item = Item.builder()
                .description("item description")
                .isAvailable(true)
                .id(ID_1)
                .name("test item")
                .owner(userOwner)
                .build();

        bookingForSave = BookingMapper.requestDtoToEntity(newBookingRequestDto, item, userOwner).orElseThrow();

        newBooking = Booking.builder()
                .booker(userBooker)
                .id(ID_1)
                .status(StatusOfBooking.WAITING)
                .start(start)
                .end(end)
                .item(item)
                .build();
    }

        @Test
    void addNewBooking_whenAddNewCorrectBookingDto_thenReturnBookingDto() {
        when(bookingMapperService.bookingRequestPrepareForAdd(ID_1, newBookingRequestDto)).thenReturn(bookingForSave);
        when(bookingRepo.save(bookingForSave)).thenReturn(newBooking);

        BookingResponseDto actualDto = bookingService.addNewBooking(ID_1, newBookingRequestDto);
        BookingResponseDto expectedDto = BookingMapper.entityToResponseDto(newBooking).orElseThrow();

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void addNewBooking_whenAddNewBookingDtoWithIncorrectBookerId_thenThrowUserNotFoundException() {
        when(bookingMapperService.bookingRequestPrepareForAdd(999L, newBookingRequestDto))
                .thenThrow(UserNotFoundException.class);
        when(bookingMapperService.bookingRequestPrepareForAdd(-999L, newBookingRequestDto))
                .thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,
                () -> bookingService.addNewBooking(999L, newBookingRequestDto));
        assertThrows(UserNotFoundException.class,
                () -> bookingService.addNewBooking(-999L, newBookingRequestDto));

        verify(bookingMapperService).bookingRequestPrepareForAdd(999L, newBookingRequestDto);
        verify(bookingRepo, never()).save(bookingForSave);
    }

    @Test
    void approveBooking_whenCorrectBookingAndStatus_thenReturnDtoWithApprovedStatus() {
        Booking bookingWithStatus = newBooking;
        bookingWithStatus.setStatus(StatusOfBooking.APPROVED);
        when(bookingMapperService.addStatusToBooking(ID_1, ID_2, Boolean.TRUE)).thenReturn(bookingWithStatus);
        when(bookingRepo.save(bookingWithStatus)).thenReturn(bookingWithStatus);

        BookingResponseDto actualDto = bookingService.approveBooking(ID_1, ID_2, Boolean.TRUE);
        BookingResponseDto expectedDto = BookingMapper.entityToResponseDto(bookingWithStatus).orElseThrow();

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void approveBooking_whenCorrectBookingAndNullStatus_thenThrowValidationException() {
        when(bookingMapperService.addStatusToBooking(ID_1, ID_2, null))
                .thenThrow(ValidationException.class);
        assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(ID_1, ID_2, null));
    }

        @Test
    void getBooking() {
    }

    @Test
    void getBookings() {
    }

    @Test
    void getListOfBookingsOfOwnersItems() {
    }
}