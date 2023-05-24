package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.user.model.UserService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/items")
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
                       @PathVariable long itemId,
                       @RequestBody ItemDto itemDto) {
        log.info("Update {} for item id: {} by user id {}  - Started", itemDto, itemId, userId);
        userService.getUser(userId);                     // проверяем наличие в базе такого юзера
        Item item = itemService.updateItem(userId, itemId, itemDto);
        log.info("update: {} - Finished", item);
        return item;
    }

    @GetMapping
    public Collection<Item> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GetItems for user id {} - Started", userId);
        Collection<Item> itemsOfUser = itemService.getItems(userId);
        log.info("Found {} items of user id {} - GetItems Finished", itemsOfUser.size(), userId);
        return itemsOfUser;
    }

    @GetMapping("/{itemId}")
    public Item getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                        @PathVariable long itemId) {
        log.info("Search for item id {} - Started", itemId);
        Item item = itemService.getItem(itemId);
        log.info("item {} was found", item);
        return item;
    }

    @GetMapping("/search")
    public Collection<Item> searchItems(@RequestParam String text) {
        log.info("Search for available items with text '{}' - Started", text);
        Collection<Item> searchResult = itemService.searchForItems(text);
        log.info("{} available items with text '{}' was found - Finished", searchResult.size(), text);
        return searchResult;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long itemId) {
        log.info("Delete item id {} user id {} - Started", itemId, userId);
        boolean isDel = itemService.deleteItem(userId, itemId);
        log.info("item id {} was deleted - {} ", itemId, isDel);

    }

    @DeleteMapping
    public void clearAll() {
        log.info("Total clear - Started");
        itemService.clearAll();
        log.info("All items was deleted");
    }
}
