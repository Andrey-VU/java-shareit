package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.ItemService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/items")
@Slf4j
public class ItemController {
    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                       @RequestBody ItemDto itemDto) {
        log.info("add: {} - Started", itemDto);
        ItemDto itemDtoFromRepo = itemService.addNewItem(ownerId, itemDto);
        log.info("create: {} - Finished", itemDtoFromRepo);
        return itemDtoFromRepo;
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @PathVariable Long itemId,
                          @RequestBody ItemDto itemDto) {
        log.info("Update {} for item id: {} by user id {}  - Started", itemDto, itemId, userId);
        ItemDto itemDtoFromRepo = itemService.updateItem(userId, itemId, itemDto);
        log.info("update: {} - Finished", itemDtoFromRepo);
        return itemDtoFromRepo;
    }

    @GetMapping
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("GetItems for user id {} - Started", ownerId);
        List<ItemDto> itemsOfUser = itemService.getItems(ownerId).stream()
                .map(item -> ItemMapper.makeDtoFromItem(item))
                .collect(Collectors.toList());
        log.info("Found {} items of user id {} - GetItems Finished", itemsOfUser.size(), ownerId);
        return itemsOfUser;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
        log.info("Search for item id {} - Started", itemId);
        ItemDto itemDto = itemService.getItem(itemId, userId);
        log.info("item {} was found", itemDto);
        return itemDto;
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestParam String text) {
        log.info("Search for available items with text '{}' - Started", text.toLowerCase());
        Collection<ItemDto> searchResult = itemService.searchForItems(text);
        log.info("{} available items with text '{}' was found - Finished", searchResult.size(), text.toLowerCase());
        return searchResult;
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                           @PathVariable Long itemId) {
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
