package ru.practicum.shareit.request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;

    @Test
    void addRequest() {

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("first test");

//        itemRequestDtoResponse.setId(1L);
//        itemRequestDtoResponse.setDescription("first test");
//        itemRequestDtoResponse.setCreated(LocalDateTime.now());
//        itemRequestDtoResponse.setRequesterId(1L);

        ItemRequestDto responseDto = itemRequestController.addRequest(1L, itemRequestDto);
        ResponseEntity<ItemRequestDto> response = ResponseEntity.ok(responseDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    void getAllItemRequests_whenInvokedEmpty_thenResponseStatusOkWithEmptyBody() {
        List<ItemRequestDto> responseDto = itemRequestController.getItemRequests(1L);
        ResponseEntity<List<ItemRequestDto>> response =  ResponseEntity.ok(responseDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAllItemRequests_whenInvokedNotEmpty_thenResponseStatusOkWithDtoCollectionInBody() {
        List<ItemRequestDto> expectedDto = List.of(new ItemRequestDto());
        Mockito.when(itemRequestController.getItemRequests(1L))
                .thenReturn(expectedDto);
        List<ItemRequestDto> responseDto
                = itemRequestController.getItemRequests(1L);
        ResponseEntity<List<ItemRequestDto>> response =  ResponseEntity.ok(responseDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(!response.getBody().isEmpty());
        assertEquals(expectedDto, response.getBody());
    }

    @Test //получить список своих запросов вместе с данными об ответах на них
    void getItemRequests_whenUserHasNoRequests_thenReturn200() {
        when(itemRequestController.getItemRequests(1L)).thenReturn(List.of(new ItemRequestDto()));
        List<ItemRequestDto> responseDto
                = itemRequestController.getItemRequests(1L);
        ResponseEntity<List<ItemRequestDto>> response =  ResponseEntity.ok(responseDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetItemRequests() {
    }
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
