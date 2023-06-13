package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Slf4j
public final class ItemMapper {
    public static Item makeItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setIsAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setId(itemDto.getId());
        return item;
    }

    public static Item makeItemForUpdate(ItemDto oldItemDto, ItemDto itemDtoWithUpdate, User owner) {
        Item itemUpd = new Item();

        itemUpd.setIsAvailable(oldItemDto.getAvailable());
        itemUpd.setId(oldItemDto.getId());
        itemUpd.setOwner(owner);
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
        itemDto.setOwnerId(item.getOwner().getId());

        return itemDto;
    }

    public static ItemDto makeDtoFromItemWithBooking(Item item, User owner, BookingResponseDto lastBooking,
                                                     BookingResponseDto nextBooking) {
        ItemDto itemDto = new ItemDto();
        itemDto.setAvailable(item.getIsAvailable());
        itemDto.setDescription(item.getDescription());
        itemDto.setName(item.getName());
        itemDto.setId(item.getId());
        itemDto.setOwnerId(item.getOwner().getId());
       // itemDto.setOwner(owner);

        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);

        return itemDto;
    }
}
