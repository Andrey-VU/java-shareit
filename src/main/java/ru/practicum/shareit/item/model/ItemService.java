package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Item addNewItem(long userId, ItemDto itemDto);

    Item getItem(long itemId);

    Collection<Item> getItems(long userId);

    Item updateItem(long userId, long itemId, ItemDto itemDto);

    boolean deleteItem(long userId, long itemId);

    void clearAll();

    Collection<Item> searchForItems(String text);
}