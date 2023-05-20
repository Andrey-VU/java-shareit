package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.IncorrectItemDtoException;
import ru.practicum.shareit.item.model.Item;
import org.apache.commons.lang3.StringUtils;

@Component
@Slf4j
public class ItemMapper {
    public Item makeItem(ItemDto itemDto) {
        Item item = new Item();
        String name = itemDto.getName();
        String description = itemDto.getDescription();

        if (!StringUtils.isBlank(name)) {
            item.setName(itemDto.getName());
        } else {
            log.warn("Item's name {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Item's name is not found");
        }
        if (!StringUtils.isBlank(description)) {
            item.setDescription(itemDto.getDescription());
        } else {
            log.warn("Item's description {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Item's description is not found");
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        } else {
            log.warn("Available-status of item {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Available-status of item not found");
        }
        return item;
    }
}
