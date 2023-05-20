package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    Item addNewItem(long userId, ItemDto itemDto);
    Item getItem(long id);
    List<Item> getItems(long userId);
    Item updateItem(long userId, ItemDto itemDto);
    void deleteItem(long userId, long itemId);

}