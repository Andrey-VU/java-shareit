package ru.practicum.shareit.item.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repo.ItemRepo;
import ru.practicum.shareit.item.repo.ItemRepoImpl;
import ru.practicum.shareit.user.model.UserService;
import ru.practicum.shareit.user.model.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service("itemService")
@Slf4j
public class ItemServiceImpl implements ItemService {
    ItemMapper mapper;
    ItemRepo itemRepo;
    UserService userService;

    public ItemServiceImpl(ItemMapper mapper,
                           ItemRepo itemRepo,
                           UserService userService) {
        this.mapper = mapper;
        this.itemRepo = itemRepo;
        this.userService = userService;
    }

    @Override
    public Item addNewItem(long userId, ItemDto itemDto) {
        return itemRepo.save(userId, mapper.makeItem(itemDto, userService.getUser(userId)));
    }

    @Override
    public Item getItem(long itemId) {
        validateId(itemId);
        return itemRepo.getItem(itemId);
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
        return itemRepo.getItemsOfUser(userId);
    }

    @Override
    public Item updateItem(long userId, long itemId, ItemDto itemDtoWithUpdate) {
        validateId(itemId);
        Item itemFromRepo = getItem(itemId);
        if (itemFromRepo.getOwner().getId() == userId) {
            itemDtoWithUpdate.setId(itemId);
            return itemRepo.update(userId, mapper.makeItemForUpdate(itemFromRepo, itemDtoWithUpdate));
        } else {
            log.error("User Id {} has not item", userId);
            throw new ItemNotFoundException("Item is not found");
        }
    }

    @Override
    public boolean deleteItem(long userId, long itemId) {
        userService.getUser(userId);                         // проверяем есть ли в базе
        itemRepo.delete(userId, itemId);
        return true;
    }

    @Override
    public void clearAll() {
        itemRepo.clearAll();
    }

    @Override
    public Collection<Item> searchForItems(String text) {
        Collection<Item> searchResult = new ArrayList<>();
        if (!text.isBlank()) {
            searchResult = itemRepo.getAllItems().stream()
                    .filter(item -> item.getAvailable().equals(true))
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return searchResult;
    }
}
