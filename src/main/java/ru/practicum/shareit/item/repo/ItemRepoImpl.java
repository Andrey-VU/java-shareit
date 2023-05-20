package ru.practicum.shareit.item.repo;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository("itemRepo")
public class ItemRepoImpl implements ItemRepo {
    private final Map<Long, Map<Long, Item>> itemStorageInMemory = new HashMap<>();
    private long idGenerator;

    @Override
    public Collection<Item> findByUserId(long userId) {
        return null;
    }

    @Override
    public Item save(long userId, Item item) {
        item.setId(++idGenerator);
        Map<Long, Item> mapOfItem = new HashMap<>();
        mapOfItem.put(idGenerator, item);
        itemStorageInMemory.put(userId, mapOfItem);
        return itemStorageInMemory.get(userId).get(idGenerator);
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {

    }
}
