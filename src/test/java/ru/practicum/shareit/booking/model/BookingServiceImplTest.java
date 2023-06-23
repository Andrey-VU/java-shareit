package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingMapperService;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.repo.BookingRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    BookingRequestDto newBookingRequestDto;
    final static Long ITEM_ID_1 = 1L;

    @Mock
    BookingRepository bookingRepository;
    @Mock
    BookingMapperService bookingMapperService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        newBookingRequestDto.setItemId(ITEM_ID_1);
        newBookingRequestDto.setStart(LocalDateTime.now().plusDays(1));
        newBookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));
    }

        @Test
    void addNewBooking_whenAddNewCorrectBookingDto_thenReturnBookingDto() {

    }

    @Test
    void approveBooking() {
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