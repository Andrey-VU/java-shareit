package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.item.dto.ItemMapperService;
import ru.practicum.shareit.item.model.ItemServiceImpl;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.model.UserServiceImpl;

@WebMvcTest(ItemController.class)
class ItemControllerMockitoTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemServiceImpl itemService;
    @MockBean
    private CommentRepository commentRepo;
    @MockBean
    private ItemMapperService itemMapperService;
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private BookingRepository bookingRepo;
    @MockBean
    private ItemRepository itemRepo;

    @BeforeEach
    void setUp() {


    }

    @Test
    void getItems() {

    }


}


//    @Test
//    @SneakyThrows
//    void addComment_whenUserIsNotExist_thenStatus404() {
//        CommentRequestDto commentDtoToAdd = CommentRequestDto.builder()
//                .text("noComment")
//                .itemId(1L)
//                .authorId(999L)
//                .build();
//
//        mockMvc.perform(post("/items/{itemId}/comment", 1L)
//                        .header("X-Sharer-User-Id", 999L)
//                        .contentType("application/json")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .content(objectMapper.writeValueAsString(commentDtoToAdd)))
//                .andExpect(status().isNotFound());
//        }
//}


//    CommentDto afterSave = CommentDto.builder()
//            .authorName("AuthorOfComment")
//            .text("noComment")
//            .id(1L)
//            .created(time)
//            .build();
//
//    User author = User.builder()
//            .id(1L)
//            .name("AuthorOfComment")
//            .email("a@a.a")
//            .build();
//
//    ItemRequest itemRequest = ItemRequest.builder()
//            .requester(author)
//            .description("qwer")
//            .created(time)
//            .id(1L)
//            .build();
//
//    User owner = User.builder()
//            .id(2L)
//            .name("OwnerOfItem")
//            .email("o@o.o")
//            .build();
//
//    Item item = Item.builder()
//            .owner(owner)
//            .isAvailable(true)
//            .description("desc")
//            .name("Item")
//            .request(itemRequest)
//            .build();
//
//    Comment commentToSave = Comment.builder()
//            .author(author)
//            .item(item)
//            .created(time)
//            .text("noComment")
//            .build();
//
//    Comment commentFromRepo = CommentMapper.requestToEntity(item, author, commentDtoToAdd.getText());
//        commentFromRepo.setId(1L);
//
//                List<Booking> bookingOfAuthor = List.of(Booking.builder()
//        .item(item)
//        .booker(author)
//        .status(StatusOfBooking.APPROVED)
//
//        .build());
//
//        when(itemService.addNewCommentToItem(commentDtoToAdd)).thenReturn(afterSave);
//        when(itemMapperService.prepareCommentToSave(commentDtoToAdd)).thenReturn(commentToSave);
//        when(userService.getUser(1L)).thenReturn(UserMapper.makeDto(author).orElseThrow());
//        when(bookingRepo.findAllByBookerIdAndEndBeforeOrderByStartDesc(1L, time)).thenReturn(bookingOfAuthor);
//        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
//        when(commentRepo.save(commentToSave)).thenReturn(commentFromRepo);
//
//    LocalDateTime time = LocalDateTime.now();
//    CommentRequestDto commentDtoToAdd = CommentRequestDto.builder()
//            .text("noComment")
//            .itemId(1L)
//            .authorId(1L)
//            .build();
//
//
//    String result = mockMvc.perform(post("/items/{itemId}/comment", 1L)
//                    .header("X-Sharer-User-Id", 1L)
//                    .contentType("application/json")
//                    .characterEncoding(StandardCharsets.UTF_8)
//                    .content(objectMapper.writeValueAsString(commentDtoToAdd)))
//            .andExpect(status().isOk())
//            .andReturn()
//            .getResponse()
//            .getContentAsString();
//
//    assertEquals(objectMapper.writeValueAsString(commentDtoToAdd), result);
