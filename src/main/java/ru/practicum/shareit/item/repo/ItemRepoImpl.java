package ru.practicum.shareit.item.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository("itemRepo")
@Slf4j
public class ItemRepoImpl implements ItemRepo {
    private final Map<Long, Item> itemStorageInMemory = new HashMap<>();
    private final Map<Long, List<Item>> itemsOfUsers = new HashMap<>(); // ключ - id пользователя. список - его вещи
    private long idGenerator;

    @Override
    public Map<Long, Item> findByUserId(long userId) {
        itemStorageInMemory.get(userId);
        return itemStorageInMemory.get(userId);
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
    public Item get(long itemId) {
        final Item[] item = {new Item()};
        itemStorageInMemory.values().stream()
                .filter(mapWithItem -> mapWithItem.containsKey(itemId))
                .peek(mapWithItem -> item[0] = mapWithItem.get(itemId));
        return item[0];
    }

    @Override
    public Item update(long userId, Item item) {
        Map <Long, Item> itemsOfUser = findByUserId(userId);
        if (itemsOfUser.containsKey(item.getId())) {
            findByUserId(userId).put(item.getId(), item);
            return findByUserId(userId).get(item.getId());
        } else {
            log.error("Item Id {} is not found. Update error", item.getId());
            throw new ItemNotFoundException("Item is not found");
        }
    }


    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        findByUserId(userId).remove(itemId);
//        } else {
//            log.error("Item Id {} is not found. Update error", itemId);
//            throw new ItemNotFoundException("Item is not found");
//        }
    }
}
