package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto addNewItem(Long userId, ItemDto itemDto);

    ItemDto getItem(Long itemId, Long userId);

    List<Item> getItems(Long userId);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    boolean deleteItem(Long userId, Long itemId);

    void clearAll();

    Collection<ItemDto> searchForItems(String text);


}