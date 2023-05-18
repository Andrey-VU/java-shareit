package ru.practicum.shareit.item.model;

import java.util.List;

public interface ItemService {
    Item addNewItem(long userId, Item item);
    Item getItem(long id);
    List<Item> getItems(long userId);
    Item updateItem(long userId, Item item);
    void deleteItem(long userId, long itemId);
}