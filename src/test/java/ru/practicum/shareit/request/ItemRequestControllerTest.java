package ru.practicum.shareit.request;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestServiceImpl;
import ru.practicum.shareit.request.repo.ItemRequestRepository;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    ItemRequestDto itemRequestDto = new ItemRequestDto();
    ItemRequestDto expectedRequestDto = new ItemRequestDto();
    List<ItemRequestDto> expectedListRequestDto = new ArrayList<>();

    @MockBean
    private ItemRequestServiceImpl itemRequestService;
    @MockBean
    private ItemRequestRepository itemRequestRepository;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mvc;


    @BeforeEach
    void setUp() {
        itemRequestDto.setDescription("first test");
        expectedRequestDto = itemRequestDto;
        expectedRequestDto.setId(1L);
        expectedRequestDto.setRequesterId(1L);

        expectedListRequestDto.add(expectedRequestDto);
    }

    @AfterEach
    void afterEach() {
        if (!expectedListRequestDto.isEmpty()) {
            expectedListRequestDto.clear();
        }
    }

    @Test
    void addRequest_whenRequestIsCorrect_thenReturn200AndFields() throws Exception {

        when(itemRequestService.addNewItemRequest(1L, itemRequestDto)).thenReturn(expectedRequestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.requesterId", is(itemRequestDto.getRequesterId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDto.getCreated())));
    }

    @Test
    void getItemRequests_whenUserHasRequest_thenReturn200 () throws Exception  {
        when(itemRequestService.getItemRequests(1L)).thenReturn(expectedListRequestDto);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequests_whenUserNotFound_thenReturn404 () throws Exception  {
        when(itemRequestService.getItemRequests(999L)).thenThrow(UserNotFoundException.class);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 999L))
                .andExpect(status().isNotFound());
    }

    @Test  //
    void getItemRequests_whenUserHasNotRequests_thenReturn404 () throws Exception  {
        when(itemRequestService.getItemRequests(10L)).thenThrow(ItemRequestNotFoundException.class);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 10L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemRequests_whenUserWithIncorrectId_thenReturn404 () throws Exception  {
        when(itemRequestService.getItemRequests(-1L)).thenThrow(UserNotFoundException.class);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", -1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemRequest_whenUserAndRequestAreCorrect_thenReturn200 () throws Exception  {
        when(itemRequestService.getItemRequest(1L, 1L)).thenReturn(expectedRequestDto);
        Long requestId = expectedRequestDto.getId();

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                        .andExpect(status().isOk())
//                        .andExpect(jsonPath("$.requestId").value(1L))
        ;
    }

    @Test
    void getAllItemRequests_whenCorrect_thenReturn200 () throws Exception  {
        when(itemRequestService.getAllItemRequests(1L, 1, 1))
                .thenReturn(expectedListRequestDto);
        Long requestId = expectedRequestDto.getId();

        int from = 1;
        int size = 1;

        mvc.perform(get("/requests/all/{from}&{size}", from, size)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }




//    @Test
//    void getAllItemRequests_whenInvokedEmpty_thenResponseStatusOkWithEmptyBody() {
//        List<ItemRequestDto> responseDto = itemRequestController.getItemRequests(1L);
//    }
//
//    @Test
//    void getAllItemRequests_whenInvokedNotEmpty_thenResponseStatusOkWithDtoCollectionInBody() {
//        List<ItemRequestDto> expectedDto = List.of(new ItemRequestDto());
//        Mockito.when(itemRequestController.getItemRequests(1L))
//                .thenReturn(expectedDto);
//        List<ItemRequestDto> responseDto
//                = itemRequestController.getItemRequests(1L);
//
//    }
//
//    @Test //получить список своих запросов вместе с данными об ответах на них
//    void getItemRequests_whenUserHasNoRequests_thenReturn200() {
//        when(itemRequestController.getItemRequests(1L)).thenReturn(List.of(new ItemRequestDto()));
//        List<ItemRequestDto> responseDto
//                = itemRequestController.getItemRequests(1L);
//    }
//
//    @Test
//    void testGetItemRequests() {
//    }
}



//    @MockBean
//    ItemRequestService itemRequestService;
//    @Autowired
//    ItemRequestController itemRequestController;
//    @Autowired
//    private MockMvc mvc;
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @BeforeEach
//    void setUp() {
//        mvc = MockMvcBuilders
//                .standaloneSetup(itemRequestController)
//                .build();
//    }


//    @Test
//    void shouldReturnStatusOkAndId1() throws Exception {
//         ItemRequestDto correctItemRequestDto = new ItemRequestDto();
//         correctItemRequestDto.setDescription("test description dto");
//         ItemRequestDto correctItemRequestDtoWithId = new ItemRequestDto();
//         correctItemRequestDtoWithId.setId(1L);
//         when(itemRequestService.addNewItemRequest(1L, correctItemRequestDto))
//                 .thenReturn(correctItemRequestDtoWithId);
//
//         mvc.perform(post("/requests")
//                         .header("X-Sharer-User-Id", 1L)
//                         .content(mapper.writeValueAsString(correctItemRequestDto))
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .accept(MediaType.APPLICATION_JSON)
//                         )
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$.reviewId").value("1"));
//    }

    /*
    @Test
    public void whenGetRequestResponseCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reviews"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
     */

    //    User user = new User();
//    ItemRequestDto itemRequestDto = new ItemRequestDto();
//    ItemRequest itemRequest = new ItemRequest();
//    ItemRequestDto itemRequestDtoResponse = new ItemRequestDto();

//    @BeforeEach
//    void setUp() {
//        user.setId(1L);
//        user.setName("testUser");
//        user.setEmail("test@test.ru");
//
//        itemRequest.setRequester(user);
//
//        itemRequestDto.setDescription("first test");
//
//        itemRequestDtoResponse.setId(1L);
//        itemRequestDtoResponse.setDescription("first test");
//        itemRequestDtoResponse.setCreated(LocalDateTime.now());
//        itemRequestDtoResponse.setRequesterId(1L);
//    }