package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {
    ItemService itemService;
    UserService userService;

    public ItemController(@Qualifier("itemService") ItemService itemService,
                          @Qualifier("userService") UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    public Item add(@RequestHeader("X-Sharer-User-Id") long userId,
                    @RequestBody ItemDto itemDto) {
        log.info("add: {} - Started", itemDto);
        userService.getUser(userId);                     // проверяем наличие в базе такого юзера
        Item item = itemService.addNewItem(userId, itemDto);
        log.info("create: {} - Finished", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemDto itemDto) {
        log.info("Update {} for item id: {}  - Started", itemDto, userId);
        userService.getUser(userId);                     // проверяем наличие в базе такого юзера

        Item item = itemService.updateItem(userId, itemDto);
        log.info("update: {} - Finished", item);
        return item;
    }



    @GetMapping
    public Collection<Item> get(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

}
