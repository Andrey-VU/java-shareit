package ru.practicum.shareit.item.repo;

import ru.practicum.shareit.item.model.Item;
import java.util.Collection;

public interface ItemRepo {
    Collection<Item> findByUserId(long userId);
    Item save(long userId, Item item);
    void deleteByUserIdAndItemId(long userId, long itemId);
}
