package ru.practicum.shareit.item.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
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
                User userOwner = UserMapper.makeUserWithId(userService.getUser(userId));
                // здесь будет выбрасываться 404, когда юзер будет не найден, а нам в этом методе надо,
        // чтобы было выброщено 400
        // можно попробовать вначале проверить, что там с isAvailable - тогда должно вывалиться бэд реквест

                Item item = itemRepo.save(ItemMapper.makeItem(itemDto, userOwner));
        return ItemMapper.makeDtoFromItem(item);
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
        List<Long> listOfId = new ArrayList<>();
        listOfId.add(userId);
        return itemRepo.findAllById(listOfId);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDtoWithUpdate) {
        validateId(itemId);
        validateId(userId);
        ItemDto itemFromRepo = getItem(itemId);

            itemDtoWithUpdate.setId(itemId);
            Item itemUpdated = itemRepo.save(ItemMapper.makeItemForUpdate(itemFromRepo,
                    itemDtoWithUpdate));
            return ItemMapper.makeDtoFromItem(itemUpdated);
//        } else {
//            log.error("User Id {} has not item", userId);
//            throw new ItemNotFoundException("Item is not found");
//        }
    }

    @Override
    public boolean deleteItem(long userId, long itemId) {
        userService.getUser(userId);
        itemRepo.delete(ItemMapper.makeItem(getItem(itemId),
                UserMapper.makeUserWithId(userService.getUser(userId))));
        return true;
    }

    @Override
    public void clearAll() {
        itemRepo.deleteAll();
    }

    @Override
    public List<ItemDto> searchForItems(String text) {
//        List<Long> searchResult = new ArrayList<>();
//        if (!text.isBlank()) {
//            searchResult = itemRepo.searchForItems(text);
//        }

        return itemRepo.findByNameContainingIgnoreCase(text).stream()
                .map(item -> ItemMapper.makeDtoFromItem(item))
                .collect(Collectors.toList());
    }


}
