package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemServiceImpl;

import java.util.Collection;
import java.util.List;

/*
 Эндпойнт PATCH /items/{itemId}. Изменить можно название, описание и статус доступа к аренде.
 Редактировать вещь может только её владелец.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    ItemServiceImpl itemService;

    @GetMapping
    public Collection<Item> get(@RequestHeader("X-Later-User-Id") long userId) {
        return itemService.getItems(userId);
    }

    @PostMapping
    public Item add(@RequestHeader("X-Later-User-Id") Long userId,
                    @RequestBody Item item) {
        return itemService.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Later-User-Id") long userId,
                           @PathVariable long itemId) {
        itemService.deleteItem(userId, itemId);
    }

}
