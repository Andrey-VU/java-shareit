package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.IncorrectItemDtoException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service("itemService")
@Slf4j
public class ItemServiceImpl implements ItemService {
    ItemRepository itemRepo;
    UserService userService;
    BookingRepository bookingRepo;

    @Override
    public ItemDto addNewItem(Long userId, ItemDto itemDto) {

        itemDtoValidate(userId, itemDto);
        User user = UserMapper.makeUserWithId(userService.getUser(userId));
        Item item = itemRepo.save(ItemMapper.makeItem(itemDto, user));

        return ItemMapper.makeDtoFromItem(item);
    }

    @Override
    public ItemDto getItem(Long itemId, Long userId) {
        validateId(itemId);
        validateId(userId);
        Item item = itemRepo.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Item is not found"));

        if (item.getOwner().getId().equals(userId)) {
            return getItemDtoForOwner(item, userId);
        } else {
            return getItemDtoForUser(item);
        }
    }
    public ItemDto getItemDtoForUser(Item item) {
        return ItemMapper.makeDtoFromItem(item);
    }
    public ItemDto getItemDtoForOwner(Item item, Long userId ) {
        List<Booking> allLastBooking = bookingRepo.findAllByItemIdAndStartBeforeOrderByStartAsc(item.getId(),
                LocalDateTime.now());
        List<Booking> allNextBooking = bookingRepo.findAllByItemIdAndStartAfterOrderByStartAsc(item.getId(),
                LocalDateTime.now());
        User owner = UserMapper.makeUserWithId(userService.getUser(userId));

        BookingResponseDto lastBooking = new BookingResponseDto();
        BookingResponseDto nextBooking = new BookingResponseDto();

        if (allLastBooking.size() > 0){
            lastBooking = BookingMapper.entityToResponseDto(allLastBooking.get(0));
        }

        if (allNextBooking.size() > 0) {
            nextBooking = BookingMapper.entityToResponseDto(allNextBooking.get(0));
        }

        return ItemMapper.makeDtoFromItemWithBooking(item, owner, lastBooking, nextBooking);
    }

    private void validateId(Long id) {
        if (id < 1) {
            log.warn("id {} incorrect", id);
            throw new IncorrectIdException("id can't be less then 1");
        }
    }

    @Override
    public List<Item> getItems(Long userId) {
        return itemRepo.findAll().stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDtoWithUpdate) {
        validateId(itemId);
        validateId(ownerId);
        User owner = UserMapper.makeUserWithId(userService.getUser(ownerId));
        ItemDto itemDtoFromRepo = getItemForUpdate(itemId);
        Item item = ItemMapper.makeItemForUpdate(itemDtoFromRepo, itemDtoWithUpdate, owner);
        Item itemUpdated = itemRepo.save(item);
        return ItemMapper.makeDtoFromItem(itemUpdated);
    }

    private ItemDto getItemForUpdate(Long itemId) {
        Item item = itemRepo.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException("Item is not found"));
        return getItemDtoForUser(item);
    }

    @Override
    public boolean deleteItem(Long ownerId, Long itemId) {
        User user = UserMapper.makeUserWithId(userService.getUser(ownerId));
        itemRepo.delete(ItemMapper.makeItem(getItem(itemId, ownerId), user));
        return true;
    }

    @Override
    public void clearAll() {
        itemRepo.deleteAll();
    }

    @Override
    public List<ItemDto> searchForItems(String text) {
        List<ItemDto> searchResult = new ArrayList<>();
        if (!text.isBlank()) {
            searchResult = itemRepo.findByText(text).stream()
                    .map(item -> ItemMapper.makeDtoFromItem(item))
                    .collect(Collectors.toList());
        }
        return searchResult;
    }


    private void itemDtoValidate(long userId, ItemDto itemDto) {
        String name = itemDto.getName();
        String description = itemDto.getDescription();

        if (StringUtils.isBlank(name)) {
            log.warn("Item's name {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Item's name is not found");
        }
        if (StringUtils.isBlank(description)) {
            log.warn("Item's description {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Item's description is not found");
        }
        if (itemDto.getAvailable() == null) {
            log.warn("Available-status of item {} can't be null!", itemDto);
            throw new IncorrectItemDtoException("Available-status of item not found");
        }
        userService.getUser(userId);
    }
}
