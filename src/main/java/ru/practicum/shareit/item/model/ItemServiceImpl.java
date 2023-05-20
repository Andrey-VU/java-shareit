package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repo.ItemRepoImpl;

import java.util.List;

@Service("itemService")
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{
    ItemMapper mapper;
    ItemRepoImpl itemRepo;

    @Override
    public Item addNewItem(long userId, ItemDto itemDto) {
        return itemRepo.save(userId, mapper.makeItem(itemDto));
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
    public Item updateItem(long userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
    }
}
