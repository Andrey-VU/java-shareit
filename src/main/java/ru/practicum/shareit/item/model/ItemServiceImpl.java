package ru.practicum.shareit.item.model;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    @Override
    public Item addNewItem(long userId, Item item) {
        return null;
    }

    @Override
    public Item getItem(long id) {
        return null;
    }

    @Override
    public List<Item> getItems(long userId) {
        return null;
    }

    @Override
    public Item updateItem(long userId, Item item) {
        return null;
    }

    @Override
    public void deleteItem(long userId, long itemId) {

    }
}
