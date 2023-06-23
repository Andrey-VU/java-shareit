package ru.practicum.shareit.request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestService;

import javax.print.attribute.standard.Media;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig({ ItemRequestController.class})
class ItemRequestControllerTest {

    @Mock
    private ItemRequestService itemRequestService;
    @InjectMocks
    private ItemRequestController itemRequestController;
    private final ObjectMapper mapper = new ObjectMapper();
    private MockMvc mvc;


    @Test
    void addRequest_whenRequestIsCorrect_thenReturn200() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("first test");

        ItemRequestDto responseDto = itemRequestController.addRequest(1L, itemRequestDto);
        ResponseEntity<ItemRequestDto> response = ResponseEntity.ok(responseDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());



  //              .Expect(status().isOk());
//                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
//                .andExpect(jsonPath("$.firstName", is(userDto.getFirstName())))
//                .andExpect(jsonPath("$.lastName", is(userDto.getLastName())))
//                .andExpect(jsonPath("$.email", is(userDto.getEmail())));


    }

    @Test
    void addRequest_whenRequestIsCorrect_thenReturnResponseWithBody() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("first test");

        ItemRequestDto expectedDto = new ItemRequestDto();
        expectedDto.setRequesterId(1L);
        expectedDto.setId(1L);
        expectedDto.setDescription("first test");

//        expectedDto.setCreated();

        Mockito.when(itemRequestService.addNewItemRequest(1L, itemRequestDto))
                .thenReturn(expectedDto);

        ItemRequestDto responseDto = itemRequestController.addRequest(1L, itemRequestDto);


    }


    @Test
    void getAllItemRequests_whenInvokedEmpty_thenResponseStatusOkWithEmptyBody() {
        List<ItemRequestDto> responseDto = itemRequestController.getItemRequests(1L);
    }

    @Test
    void getAllItemRequests_whenInvokedNotEmpty_thenResponseStatusOkWithDtoCollectionInBody() {
        List<ItemRequestDto> expectedDto = List.of(new ItemRequestDto());
        Mockito.when(itemRequestController.getItemRequests(1L))
                .thenReturn(expectedDto);
        List<ItemRequestDto> responseDto
                = itemRequestController.getItemRequests(1L);

    }

    @Test //получить список своих запросов вместе с данными об ответах на них
    void getItemRequests_whenUserHasNoRequests_thenReturn200() {
        when(itemRequestController.getItemRequests(1L)).thenReturn(List.of(new ItemRequestDto()));
        List<ItemRequestDto> responseDto
                = itemRequestController.getItemRequests(1L);
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
