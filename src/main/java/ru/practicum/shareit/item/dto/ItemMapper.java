package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.exception.IncorrectItemDtoException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


@Slf4j
public final class ItemMapper {
    public static Item makeItem(ItemDto itemDto, User user) {
        Item item = new Item();
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        item.setOwnerId(user.getId());

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
            item.setIsAvailable(itemDto.getAvailable());
        } else {
            log.warn("Available-status of item {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Available-status of item not found");
        }

        return item;
    }

    public static Item makeItemForUpdate(ItemDto oldItemDto, ItemDto itemDtoWithUpdate) {
        Item itemUpd = new Item();

        itemUpd.setIsAvailable(oldItemDto.getAvailable());
        itemUpd.setId(oldItemDto.getId());
//        itemUpd.setOwnerId(oldItemDto.getOwner());
        itemUpd.setDescription(oldItemDto.getDescription());
        itemUpd.setName(oldItemDto.getName());

        if (itemDtoWithUpdate.getName() != null) {
            itemUpd.setName(itemDtoWithUpdate.getName());
        }

        if (itemDtoWithUpdate.getDescription() != null) {
            itemUpd.setDescription(itemDtoWithUpdate.getDescription());
        }

        if (itemDtoWithUpdate.getAvailable() != null) {
            itemUpd.setIsAvailable(itemDtoWithUpdate.getAvailable());
        }
        return itemUpd;
    }

    public static ItemDto makeDtoFromItem(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setName(item.getName());
        itemDto.setId(item.getId());
//        itemDto.setOwner(item.getOwnerId());

        return itemDto;
    }
}
