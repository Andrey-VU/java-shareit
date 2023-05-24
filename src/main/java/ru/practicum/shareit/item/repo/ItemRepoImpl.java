package ru.practicum.shareit.item.repo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository("itemRepo")
@Slf4j
public class ItemRepoImpl implements ItemRepo {
    private final Map<Long, Item> itemStorageInMemory = new HashMap<>(); // ключ  - id item
    private final Map<Long, List<Item>> itemsOfUsers = new HashMap<>(); // ключ - id пользователя. список - его вещи
    private long idGenerator;

       @Override
    public Item save(long userId, Item item) {
        item.setId(++idGenerator);
        if (itemsOfUsers.containsKey(userId) && itemsOfUsers.get(userId).contains(item)){
            log.warn("User id {} already has item {} ", userId, item);
            throw new ConflictException("item already exists");
        }

        itemStorageInMemory.put(item.getId(), item);

        if (itemsOfUsers.containsKey(userId)) {
            itemsOfUsers.get(userId).add(item);
        } else {
            List<Item> myItems = new ArrayList<>();
            myItems.add(item);
            itemsOfUsers.put(userId, myItems);
        }

        return itemStorageInMemory.get(item.getId());
    }

    @Override
    public Item getItem(long itemId) {
        if (itemStorageInMemory.containsKey(itemId)) {
            return itemStorageInMemory.get(itemId);
        } else {
            log.error("Item Id {} is not found. Update error", itemId);
            throw new ItemNotFoundException("Item is not found");
        }
    }

    @Override
    public Item update(long userId, Item item) {
       if (itemStorageInMemory.containsKey(item.getId())) {
           itemStorageInMemory.put(item.getId(), item);
       } else {
           log.error("Item Id {} is not found. Update error", item.getId());
           throw new ItemNotFoundException("Item is not found");
       }
       itemsOfUsers.get(userId).remove(getItem(item.getId()));
       itemsOfUsers.get(userId).add(item);
    return getItem(item.getId());
    }

    @Override
    public void delete(long userId, long itemId) {
        itemsOfUsers.get(userId).remove(getItem(itemId));
        itemStorageInMemory.remove(itemId);
    }

    @Override
    public Collection<Item> getItemsOfUser(long userId) {
        if (itemsOfUsers.containsKey(userId)) {
            return itemsOfUsers.get(userId);
        } else {
            log.warn("User {} has not items ", userId);
            throw new ItemNotFoundException("Items of User " + userId + " are NOT FOUND");
        }
    }

    @Override
    public Collection<Item> getAllItems() {
        return itemStorageInMemory.values();
    }

    @Override
    public void clearAll() {
       if (!itemStorageInMemory.isEmpty()) {
           itemStorageInMemory.clear();
           log.info("Хранилище вещей очищено");
       }
       if (!itemsOfUsers.isEmpty()) {
           itemsOfUsers.clear();
           log.info("Список вещей всех пользователей очищен");
       }
    }
}
