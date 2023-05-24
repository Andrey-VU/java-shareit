package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.IncorrectItemDtoException;
import ru.practicum.shareit.item.model.Item;
import org.apache.commons.lang3.StringUtils;
import ru.practicum.shareit.user.model.User;

@Component
@Slf4j
public class ItemMapper {
    public Item makeItem(ItemDto itemDto, User user) {
        Item item = new Item();
        String name = itemDto.getName();
        String description = itemDto.getDescription();

        item.setOwner(user);

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


    public Item makeItemForUpdate(Item oldItem, ItemDto itemDtoWithUpdate) {
        Item itemUpd = oldItem;
        itemUpd.setId(itemDtoWithUpdate.getId());

        if (itemDtoWithUpdate.getName() != null) {
            itemUpd.setName(itemDtoWithUpdate.getName());
        }

        if (itemDtoWithUpdate.getDescription() != null) {
            itemUpd.setDescription(itemDtoWithUpdate.getDescription());
        }

        if (itemDtoWithUpdate.getAvailable() != null) {
            itemUpd.setAvailable(itemDtoWithUpdate.getAvailable());
        }
        return itemUpd;
    }
}
