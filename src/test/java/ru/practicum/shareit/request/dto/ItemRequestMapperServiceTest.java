package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperServiceTest {
    private ItemRequestDto dtoToPrepareItemRequest;
    private UserDto userDtoRequester;
    private User requester;
    private ItemRequest expectedItemRequest;

    @Mock
    private UserServiceImpl userService;
    @Mock
    private ItemRepository itemRepo;
    @InjectMocks
    private ItemRequestMapperService itemRequestMapperService;

    @BeforeEach
    void setUp() {
        dtoToPrepareItemRequest = ItemRequestDto.builder()
                .description("description")
                .build();

        userDtoRequester = UserDto.builder()
                .id(1L)
                .name("Requester")
                .email("r@r.r")
                .build();

        requester = UserMapper.makeUserWithId(userDtoRequester).orElseThrow();

        expectedItemRequest = ItemRequest.builder()
                .requester(requester)
                .description(dtoToPrepareItemRequest.getDescription())
                .build();
    }

    @Test
    void prepareForSaveItemRequest_whenIncomeCorrect_thenReturnEntity() {
        when(userService.getUser(1L)).thenReturn(userDtoRequester);
        assertEquals(expectedItemRequest,
                itemRequestMapperService.prepareForSaveItemRequest(1L, dtoToPrepareItemRequest));
        verify(userService, times(1)).getUser(1L);
    }

    @Test
    void prepareForSaveItemRequest_whenUserNotFound_thenThrowsException() {
        when(userService.getUser(999L)).thenThrow(UserNotFoundException.class);
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> itemRequestMapperService.prepareForSaveItemRequest(999L, dtoToPrepareItemRequest));
        userNotFoundException.getMessage();

        verify(userService, times(1)).getUser(999L);
    }

    @Test
    void prepareForSaveItemRequest_whenUserIncorrect_thenThrowsException() {
        when(userService.getUser(-999L)).thenThrow(UserNotFoundException.class);
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class,
                () -> itemRequestMapperService.prepareForSaveItemRequest(-999L, dtoToPrepareItemRequest));
        userNotFoundException.getMessage();

        verify(userService, times(1)).getUser(-999L);
    }

    @Test
    void requesterValidate_whenUserIdCorrect_thenReturnTrue() {
        when(userService.getUser(1L)).thenReturn(userDtoRequester);
        assertTrue(itemRequestMapperService.requesterValidate(1L));
    }

    @Test
    void requesterValidate_whenUserIdNotValid_thenReturnFalse() {
        IncorrectIdException incorrectIdException = assertThrows(IncorrectIdException.class,
                () -> itemRequestMapperService.requesterValidate(-1L));
        incorrectIdException.getMessage();

        verify(userService, never()).getUser(-1L);
    }

    @Test
    void prepareForReturnDto() {
    }

    @Test
    void prepareForReturnListDto() {
    }
}