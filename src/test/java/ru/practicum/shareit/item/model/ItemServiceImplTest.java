package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    private LocalDateTime time;
    private CommentRequestDto commentDtoToAdd;
    private CommentDto afterSave;
    private User author;
    private ItemRequest itemRequest;
    private User owner;
    private Item item;
    private Comment commentToSave;
    private Comment commentFromRepo;

    @Mock
    private ItemRepository itemRepo;
    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepo;
    @Mock
    private ItemMapperService itemMapperService;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        time = LocalDateTime.now();

        commentDtoToAdd = CommentRequestDto.builder()
                .text("noComment")
//                .itemId(1L)
//                .authorId(1L)
                .build();


        afterSave = CommentDto.builder()
                .authorName("AuthorOfComment")
                .text("noComment")
                .id(1L)
                .created(time)
                .build();

        author = User.builder()
                .id(1L)
                .name("AuthorOfComment")
                .email("a@a.a")
                .build();

        itemRequest = ItemRequest.builder()
                .requester(author)
                .description("qwer")
                .created(time)
                .id(1L)
                .build();

        owner = User.builder()
                .id(2L)
                .name("OwnerOfItem")
                .email("o@o.o")
                .build();

        item = Item.builder()
                .owner(owner)
                .isAvailable(true)
                .description("desc")
                .name("Item")
                .request(itemRequest)
                .build();

        commentToSave = Comment.builder()
                .author(author)
                .item(item)
                .created(time)
                .text("noComment")
                .build();

        commentFromRepo = CommentMapper.requestToEntity(item, author, commentDtoToAdd.getText());
        commentFromRepo.setId(1L);
    }

//    @Test
//    void addNewItem() {
//    }

    @Test
    void getItem_whenInputCorrect_thenReturnItemDto() {
        ItemDto itemDto = ItemMapper.makeDtoFromItem(item).orElseThrow();
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(itemMapperService.getItemDto(item, 1L)).thenReturn(itemDto);

        ItemDto expected = itemDto;
        ItemDto actual = itemService.getItem(1L, 1L);

        assertEquals(expected, actual);
    }

    @Test
    void getItem_whenItemIdIsNotValid_thenThrowIncorrectIdException() {
        IncorrectIdException incorrectIdException
                = assertThrows(IncorrectIdException.class, () -> itemService.getItem(-1L, 1L));
        incorrectIdException.getMessage();
    }

    @Test
    void getItem_whenUserIdIsNotValid_thenThrowIncorrectIdException() {
        assertThrows(IncorrectIdException.class, () -> itemService.getItem(1L, -1L));
    }

    @Test
    void getItem_whenItemIdIsNotExists_thenThrowsItemNot() {
        when(itemRepo.findById(999L)).thenThrow(ItemNotFoundException.class);
        ItemNotFoundException itemNotFoundException
                = assertThrows(ItemNotFoundException.class, () -> itemService.getItem(999L, 1L));

        itemNotFoundException.getMessage();
    }

    @Test
    void getItem_whenUserIdIsNotExists_thenThrowsUserNotFoundException() {
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(itemMapperService.getItemDto(item, 999L)).thenThrow(UserNotFoundException.class);
        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> itemService.getItem(1L, 999L));
        userNotFoundException.getMessage();
    }


    @Test
    void getItems() {
        List<Item> allItems = List.of(item);
        List<ItemDto> allItemsDto = allItems.stream()
                .map(item1 -> ItemMapper.makeDtoFromItem(item1)
                        .orElseThrow())
                .collect(Collectors.toList());
        when(itemRepo.findAllByOwnerIdOrderById(1L)).thenReturn(allItems);
        when(itemMapperService.getItems(allItems)).thenReturn(allItemsDto);

        assertEquals(allItemsDto, itemService.getItems(1L));
    }


    @Test
    void searchForItems_whenTextIsEmpty_thenReturnEmptyList() {
        List<ItemDto> searchDtoResult = new ArrayList<>();
        assertEquals(searchDtoResult, itemService.searchForItems(""));
    }

    @Test
    void searchForItems_whenTextIsCorrect_thenReturnDtoList() {
        String text = "des";
        List<ItemDto> searchDtoResult = List.of(ItemMapper.makeDtoFromItem(item).orElseThrow());
        List<Item> searchEntityResult = List.of(item);
        when(itemRepo.findByText(text)).thenReturn(searchEntityResult);

        assertEquals(searchDtoResult, itemService.searchForItems(text));
    }

    @Test
    void addNewCommentToItem() {
        when(itemMapperService.prepareCommentToSave(commentDtoToAdd))
                .thenReturn(commentFromRepo);
        when(commentRepo.save(commentFromRepo)).thenReturn(commentFromRepo);
        assertEquals(CommentMapper.entityToDto(commentFromRepo), itemService.addNewCommentToItem(commentDtoToAdd));
    }

    @Test
    void deleteItem() {
        when(userService.getUser(2L)).thenReturn(UserMapper.makeDto(owner).orElseThrow());
        when(itemRepo.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(itemMapperService.getItemDto(item, 2L)).thenReturn(ItemMapper.makeDtoFromItem(item).orElseThrow());
        itemService.deleteItem(2L, 1L);
        verify(itemRepo).delete(item);
    }

    @Test
    void clearAll() {
        itemService.clearAll();
        verify(itemRepo).deleteAll();
    }
}