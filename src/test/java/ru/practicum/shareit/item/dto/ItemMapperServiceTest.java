package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.IncorrectItemDtoException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemMapperServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepo;
    @Mock
    private BookingRepository bookingRepo;
    @Mock
    private ItemRepository itemRepo;
    @Mock
    private ItemRequestRepository itemRequestRepo;
    @InjectMocks
    private ItemMapperService itemMapperService;

    private User userOwner;
    private User userBooker;
    private Item item;
    private Long ownerId = 1L;
    private Long bookerId = 2L;
    private ItemDto itemDtoValid;
    private User userRequester;
    private UserDto userDtoRequester;
    private ItemRequest itemRequest;
    private LocalDateTime created = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        userOwner = User.builder()
                .id(ownerId)
                .email("o@o.ru")
                .name("OwnerUser")
                .build();

        userBooker = User.builder()
                .id(bookerId)
                .email("b@b.ru")
                .name("BookerUser")
                .build();

        userDtoRequester = UserDto.builder()
                .id(1L)
                .name("Requester")
                .email("r@r.r")
                .build();

        userRequester = UserMapper.makeUserWithId(userDtoRequester).orElseThrow();

        itemRequest = ItemRequest.builder()
                .requester(userRequester)
                .description("description")
                .created(created)
                .build();

        item = Item.builder()
                .description("item description")
                .isAvailable(true)
                .id(1L)
                .name("test item")
                .owner(userOwner)
                .build();

        itemDtoValid = ItemDto.builder()
                .available(true)
                .name(item.getName())
                .description(item.getDescription())
                .requestId(1L)
                .build();

    }

    @Test
    void addNewItem_whenNameNotValid_thenThrowIncorrectItemDtoException() {
        ItemDto itemDtoWithoutName = ItemDto.builder()
                .available(true)
                .description(item.getDescription())
                .build();

        IncorrectItemDtoException ex = assertThrows(IncorrectItemDtoException.class,
                () -> itemMapperService.addNewItem(ownerId, itemDtoWithoutName));
        ex.getMessage();
    }

    @Test
    void addNewItem_whenNotAvailable_thenThrowIncorrectItemDtoException() {
        ItemDto itemDtoNotValid = ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .build();

        IncorrectItemDtoException ex = assertThrows(IncorrectItemDtoException.class,
                () -> itemMapperService.addNewItem(ownerId, itemDtoNotValid));
        ex.getMessage();
    }

    @Test
    void addNewItem_whenNotDescription_thenThrowIncorrectItemDtoException() {
        ItemDto itemDtoNotValid = ItemDto.builder()
                .available(true)
                .name(item.getName())
                .description("")
                .build();

        IncorrectItemDtoException ex = assertThrows(IncorrectItemDtoException.class,
                () -> itemMapperService.addNewItem(ownerId, itemDtoNotValid));
        ex.getMessage();
    }

    @Test
    void addNewItem_whenRequestCorrect_thenReturnEntity() {
        UserDto userDto = UserMapper.makeDto(userOwner).orElseThrow();
        when(userService.getUser(ownerId)).thenReturn(userDto);
        when(itemRequestRepo.findById(1L)).thenReturn(Optional.ofNullable(itemRequest));

        assertEquals(item, itemMapperService.addNewItem(ownerId, itemDtoValid));
    }

    @Test
    void getItemDto() {
    }

    @Test
    void getItems() {
    }

    @Test
    void getItemDtoForUser() {
    }

    @Test
    void getItemDtoForOwner() {
    }

    @Test
    void prepareItemToUpdate() {
    }
}