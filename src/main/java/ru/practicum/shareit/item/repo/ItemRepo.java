package ru.practicum.shareit.item.repo;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepo {

    Item save(long userId, Item item);

    Item getItem(long itemId);

    Item update(long userId, Item item);

    void delete(long userId, long itemId);

    Collection<Item> getItemsOfUser(long userId);

    Collection<Item> getAllItems();

    void clearAll();
}
