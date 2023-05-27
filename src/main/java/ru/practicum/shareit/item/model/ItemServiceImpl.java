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
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.UserService;
import ru.practicum.shareit.user.model.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("itemService")
@Slf4j
public class ItemServiceImpl implements ItemService {
    ItemRepo itemRepo;
    UserService userService;

    public ItemServiceImpl(
                           ItemRepo itemRepo,
                           UserService userService) {
        this.itemRepo = itemRepo;
        this.userService = userService;
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        Item item = itemRepo.save(userId, ItemMapper.makeItem(itemDto,
                UserMapper.makeUserWithId(userService.getUser(userId))));
        return ItemMapper.makeDtoFromItem(item);
    }

    @Override
    public ItemDto getItem(long itemId) {
        validateId(itemId);
        Item item = itemRepo.getItem(itemId);
        return ItemMapper.makeDtoFromItem(item);
    }

    private void validateId(long id) {
        if (id < 1) {
            log.warn("id {} incorrect", id);
            throw new IncorrectIdException("id can't be less then 1");
        }
    }

    @Override
    public Collection<ItemDto> getItems(long userId) {
        userService.getUser(userId);                         // проверяем есть ли в базе
        return itemRepo.getItemsOfUser(userId);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDtoWithUpdate) {
        validateId(itemId);
        ItemDto itemFromRepo = getItem(itemId);
        if (itemFromRepo.getOwner().getId() != null && itemFromRepo.getOwner().getId() == userId) {
            // Здесь у пользователя не установлен до сих пор ид...  выкидывает ошибку

            itemDtoWithUpdate.setId(itemId);
            Item itemUpdated = itemRepo.update(userId, ItemMapper.makeItemForUpdate(itemFromRepo, itemDtoWithUpdate));
            return ItemMapper.makeDtoFromItem(itemUpdated);
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
    public List<ItemDto> searchForItems(String text) {
        List<ItemDto> searchResult = new ArrayList<>();
        if (!text.isBlank()) {
            searchResult = itemRepo.getAllItems().stream()
                    .filter(itemDto -> itemDto.getAvailable().equals(true))
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                            item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList());
        }
        return searchResult;
    }
}
