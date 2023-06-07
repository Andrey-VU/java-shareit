package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto getItem(long itemId);

    List<Item> getItems(long userId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    boolean deleteItem(long userId, long itemId);

    void clearAll();

    Collection<ItemDto> searchForItems(String text);


}