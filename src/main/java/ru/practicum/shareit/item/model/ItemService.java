package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto addNewItem(long userId, ItemDto itemDto);

    ItemDto getItem(long itemId);

    Collection<ItemDto> getItems(long userId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    boolean deleteItem(long userId, long itemId);

    void clearAll();

    Collection<ItemDto> searchForItems(String text);
}