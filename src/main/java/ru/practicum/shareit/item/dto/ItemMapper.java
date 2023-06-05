package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;

@Slf4j
public final class ItemMapper {
    public static Item makeItem(ItemDto itemDto, long userId) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        item.setOwnerId(userId);
        return item;
    }

    public static Item makeItemForUpdate(ItemDto oldItemDto, ItemDto itemDtoWithUpdate, long idOwner) {
        Item itemUpd = new Item();

        itemUpd.setIsAvailable(oldItemDto.getAvailable());
        itemUpd.setId(oldItemDto.getId());
        itemUpd.setOwnerId(idOwner);
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
