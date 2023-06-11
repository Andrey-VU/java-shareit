package ru.practicum.shareit.item.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.IncorrectItemDtoException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.model.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("itemService")
@Slf4j
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepo;
    UserService userService;

    public ItemServiceImpl(
            ItemRepository itemRepo,
            UserService userService) {
        this.itemRepo = itemRepo;
        this.userService = userService;
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {

        itemDtoValidate(userId, itemDto);
        Item item = itemRepo.save(ItemMapper.makeItem(itemDto, userId));

        return ItemMapper.makeDtoFromItem(item);
    }

    private void itemDtoValidate(long userId, ItemDto itemDto) {
        String name = itemDto.getName();
        String description = itemDto.getDescription();

        if (StringUtils.isBlank(name)) {
            log.warn("Item's name {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Item's name is not found");
        }
        if (StringUtils.isBlank(description)) {
            log.warn("Item's description {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Item's description is not found");
        }
        if (itemDto.getAvailable() == null) {
            log.warn("Available-status of item {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Available-status of item not found");
        }
        userService.getUser(userId);
    }

    @Override
    public ItemDto getItem(long itemId) {
        validateId(itemId);
        Item item = itemRepo.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Item is not found"));
        return ItemMapper.makeDtoFromItem(item);
    }

    private void validateId(long id) {
        if (id < 1) {
            log.warn("id {} incorrect", id);
            throw new IncorrectIdException("id can't be less then 1");
        }
    }

    @Override
    public List<Item> getItems(long userId) {
        return itemRepo.findAll().stream()
                .filter(item -> item.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDtoWithUpdate) {
        validateId(itemId);
        validateId(userId);
        ItemDto itemDtoFromRepo = getItem(itemId);
        Item item = ItemMapper.makeItemForUpdate(itemDtoFromRepo, itemDtoWithUpdate, userId);
        Item itemUpdated = itemRepo.save(item);
        return ItemMapper.makeDtoFromItem(itemUpdated);
    }

    @Override
    public boolean deleteItem(long userId, long itemId) {
        userService.getUser(userId);
        itemRepo.delete(ItemMapper.makeItem(getItem(itemId), userId));
        return true;
    }

    @Override
    public void clearAll() {
        itemRepo.deleteAll();
    }

    @Override
    public List<ItemDto> searchForItems(String text) {
        List<ItemDto> searchResult = new ArrayList<>();
        if (!text.isBlank()) {
            searchResult = itemRepo.findByText(text).stream()
                    .map(item -> ItemMapper.makeDtoFromItem(item))
                    .collect(Collectors.toList());
        }
        return searchResult;
    }
}
