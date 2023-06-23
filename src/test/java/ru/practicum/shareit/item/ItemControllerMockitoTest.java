package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.model.ItemRequestService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemControllerMockitoTest {

    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;

    @Test
    void add() {
    }

    @Test
    void addComment() {
    }

    @Test
    void update() {
    }

    @Test
    void getItems() {
    }

    @Test
    void getItem() {
    }

    @Test
    void searchItems() {
    }
}