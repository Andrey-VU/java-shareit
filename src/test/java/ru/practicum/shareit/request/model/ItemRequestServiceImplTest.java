package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestMapperService;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    private List<ItemRequest> expectedList = new ArrayList<>();
    private List<ItemRequestDto> expectedListDto = new ArrayList<>();
    private final ItemRequestDto itemRequestDto = new ItemRequestDto();
    private final ItemRequestDto itemRequestDtoResponse = new ItemRequestDto();
    private ItemRequestDto expectedItemRequestDto = new ItemRequestDto();
    private final ItemRequest itemRequest = new ItemRequest();
    private final ItemRequest expectedItemRequest = new ItemRequest();
    private final User user = new User();
    final long itemRequestId1 = 1L;
    final long requesterId1 = 1L;

    @Mock
    private ItemRequestMapperService itemRequestMapperService;
    @Mock
    private ItemRequestRepository itemRequestRepo;
    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;


    @BeforeEach
    void setUp() {
        user.setId(requesterId1);
        user.setName("testUser");
        user.setEmail("test@test.ru");

        itemRequest.setRequester(user);
        itemRequestDto.setDescription("first test");

        itemRequestDtoResponse.setId(1L);
        itemRequestDtoResponse.setDescription("first test");
        itemRequestDtoResponse.setCreated(LocalDateTime.now());
        itemRequestDtoResponse.setRequesterId(1L);

        expectedItemRequest.setId(itemRequestId1);
        expectedItemRequest.setRequester(user);
        expectedItemRequest.setDescription("first test");
        expectedItemRequest.setCreated(LocalDateTime.now());
        expectedItemRequestDto = ItemRequestMapper.makeItemRequestDto(expectedItemRequest).orElseThrow();

        expectedList.add(expectedItemRequest);
        expectedListDto.add(expectedItemRequestDto);
    }

    @AfterEach
    void afterEach() {
        if (!expectedList.isEmpty()) {
            expectedList.clear();
        }
        if (!expectedListDto.isEmpty()) {
            expectedListDto.clear();
        }
    }

    @Test
    void getItemRequest_whenEntityFound_thenReturnDto() {
        when(itemRequestRepo.findById(itemRequestId1)).thenReturn(Optional.of(expectedItemRequest));
        ItemRequestDto actualRequestDto = itemRequestService.getItemRequest(requesterId1, itemRequestId1);
        assertEquals(expectedItemRequestDto, actualRequestDto);
    }

    @Test
    void addNewItemRequest_whenRequestIsCorrect_thenReturnDto() {
        when(itemRequestMapperService.prepareForSaveItemRequest(requesterId1, itemRequestDto))
                .thenReturn(expectedItemRequest);
        ItemRequest expectedItemRequestForSave = expectedItemRequest;
        expectedItemRequestForSave.setId(null);

        when(itemRequestRepo.save(expectedItemRequestForSave)).thenReturn(expectedItemRequest);

        ItemRequestDto actualRequestDto = itemRequestService.addNewItemRequest(requesterId1, itemRequestDto);
        assertEquals(expectedItemRequestDto, actualRequestDto);
    }

    @Test
    void addNewItemRequest_withWrongUser_thenUserNotFoundExceptionThrown() {
        when(itemRequestMapperService.prepareForSaveItemRequest(999L, itemRequestDto))
                .thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,
                () -> itemRequestService.addNewItemRequest(999L, itemRequestDto));
    }

    @Test
    void addNewItemRequest_withoutDescription_thenValidationExceptionExceptionThrown() {
        itemRequestDto.setDescription(null);
        when(itemRequestMapperService.prepareForSaveItemRequest(requesterId1, itemRequestDto))
                .thenThrow(ValidationException.class);
        // когда void, нужно делать doThrow

        assertThrows(ValidationException.class,
                () -> itemRequestService.addNewItemRequest(requesterId1, itemRequestDto));
        verify(itemRequestMapperService).prepareForSaveItemRequest(requesterId1, itemRequestDto);
        verify(itemRequestRepo, never()).save(itemRequest);
    }




    @Test //получить список своих запросов вместе с данными об ответах на них
    void getItemRequests_whenRequestIsFound_thenReturnListWithDto() {
        when(itemRequestRepo.findAllByRequesterIdOrderByCreatedDesc(requesterId1)).thenReturn(expectedList);
        List<ItemRequestDto> actual = itemRequestService.getItemRequests(requesterId1);
        assertEquals(expectedListDto, actual);
    }

    @Test //получить список своих запросов вместе с данными об ответах на них
    void getItemRequests_whenUserHasNoRequests_thenReturnEmptyList() {
        when(itemRequestRepo.findAllByRequesterIdOrderByCreatedDesc(requesterId1))
                .thenReturn(List.of(new ItemRequest()));
        List<ItemRequestDto> actual = itemRequestService.getItemRequests(requesterId1);
        assertEquals(List.of(new ItemRequestDto()), actual);
    }

    @Test
    void getAllItemRequests() {

//        Page<ItemRequest> expectedPage = expectedList.stream()
//                        .map(itemRequest1 -> createPageRequestUsing)
//
//        when(itemRequestRepo.findAll(PageRequest.of(1, 1)))
//                .thenReturn((Page<ItemRequest>) expectedList);
//        List<ItemRequestDto> actual = itemRequestService.getAllItemRequests(requesterId1, 1, 1);
//        assertEquals(expectedListDto, actual);
    }
}