package ru.practicum.shareit.item.repo;

import ru.practicum.shareit.item.model.Item;

import java.util.Map;
import java.util.Optional;

public interface ItemRepo {
    Map findByUserId(long userId);

    Item save(long userId, Item item);

    Item get(long itemId);

    Item update(long userId, Item item);

    void deleteByUserIdAndItemId(long userId, long itemId);
}
