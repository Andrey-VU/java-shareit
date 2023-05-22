package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repo.ItemRepoImpl;
import ru.practicum.shareit.user.model.UserServiceImpl;

import java.util.Collection;

@Service("itemService")
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{
    ItemMapper mapper;
    ItemRepoImpl itemRepo;
    UserServiceImpl userService;

    @Override
    public Item addNewItem(long userId, ItemDto itemDto) {
        return itemRepo.save(userId, mapper.makeItem(itemDto));
    }

    @Override
    public Item getItem(long userId, long itemId) {
        validateId(userId);
        validateId(itemId);
        userService.getUser(userId);                         // проверяем есть ли в базе

        if (itemRepo.findByUserId(userId).containsKey(itemId)) {
            return itemRepo.findByUserId(userId).get(itemId);
        } else {
            log.error("Item Id {} is not found. Update error", itemId);
            throw new ItemNotFoundException("Item is not found");
        }


//        if (itemRepo.findByUserId(userId).containsKey(itemId)) {
//            Item item = itemRepo.findByUserId(userId).get(itemId);
//            log.info("Item {} was found in storage", item);
//            return item;
//        } else {
//            log.error("Item Id {} is not found. Update error", itemId);
//            throw new ItemNotFoundException("Item is not found");
//        }

    }

    private void validateId(long id) {
        if (id < 1) {
            log.warn("id {} incorrect", id);
            throw new IncorrectIdException("id can't be less then 1");
        }
    }

    @Override
    public Collection<Item> getItems(long userId) {
        userService.getUser(userId);                         // проверяем есть ли в базе
        if (itemRepo.findByUserId(userId) == null) {
            log.warn("User {} has not items ", userId);
            throw new ItemNotFoundException("Items of User " + userId + " are NOT FOUND");
        }
        return itemRepo.findByUserId(userId).values();
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemDto itemDtoWithUpdate) {
        validateId(itemId);
        isUserHasItem(userId, itemId);
        Item oldItem = getItem(userId, itemId);
        itemDtoWithUpdate.setId(itemId);
        return itemRepo.update(userId, mapper.makeItemForUpdate(oldItem, itemDtoWithUpdate));
    }

    private void isUserHasItem(long userId, long id) {
        if (getItems(userId).isEmpty()) {
            log.warn("User {} has not items", userId);
            throw new ItemNotFoundException("Item " + id + " NOT FOUND");
        }
        if (!getItems(userId).contains(getItem(userId, id))) {
            log.warn("User {} has not item {} ", userId, id);
            throw new ItemNotFoundException("Item " + id + " NOT FOUND");
        }
    }

    @Override
    public boolean deleteItem(long userId, long itemId) {
        userService.getUser(userId);                         // проверяем есть ли в базе
        itemRepo.deleteByUserIdAndItemId(userId, itemId);
        return true;
    }
}
