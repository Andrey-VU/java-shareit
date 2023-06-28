package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.model.ItemService;

@ExtendWith(MockitoExtension.class)
class ItemControllerMockitoTest {

    private final ObjectMapper mapper = new ObjectMapper();
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ItemController itemController;
    @Autowired
    private MockMvc mvc;

    @Test
    void getItems() {
    }


}