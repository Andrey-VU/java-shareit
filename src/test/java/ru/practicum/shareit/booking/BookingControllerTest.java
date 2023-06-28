package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.BookingServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
    LocalDateTime start;
    LocalDateTime end;
    User booker;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(2);
        booker = User.builder()
                .email("a@a.a")
                .name("Booker")
                .id(1L)
                .build();


    }
//
//    @Test
//    @SneakyThrows
//    void add() {
//        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
//                .start(start)
//                .end(end)
//                .build();
//
//        BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
//                .start(start)
//                .end(end)
//                .id(1L)
//                .booker(UserMapper.makeDto(booker).orElseThrow())
//                .status(StatusOfBooking.WAITING)
//                .build();
//
//       mockMvc.perform(post("/bookings")
//                    .header("X-Sharer-User-Id", 1L)
//                    .contentType("application/json")
//                    .characterEncoding(StandardCharsets.UTF_8)
//                    .content(objectMapper.writeValueAsString(bookingRequestDto)))
//            .andExpect(status().isOk());
//
//       when(bookingService.addNewBooking(1L, bookingRequestDto)).thenReturn(bookingResponseDto);
//    }
//
//    @Test
//    void approve() {
//    }
//
//    @Test
//    void getBooking() {
//    }
//
//    @Test
//    void getBookings() {
//    }
//
//    @Test
//    void getBookingsOfOwnersItems() {
//    }
}