package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class ItemRequestMapperService {
    UserService userService;
    ItemRepository itemRepo;

    public ItemRequest prepareForSaveItemRequest(Long requesterId, ItemRequestDto dto) {
        User requester = UserMapper.makeUserWithId(userService.getUser(requesterId)).orElseThrow(() ->
                new UserNotFoundException("Пользователь не обнаружен"));
        return ItemRequestMapper.makeItemRequest(dto, requester).orElseThrow(() ->
                new NullPointerException("ItemRequest not found"));
    }

    public void requesterValidate(Long requesterId) {
        validateId(requesterId);
        userService.getUser(requesterId);
    }


    private void validateId(Long id) {
        if (id < 1) {
            log.warn("id {} incorrect", id);
            throw new IncorrectIdException("id can't be less then 1");
        }
    }

    public ItemRequestDto prepareForReturnDto(ItemRequest itemRequest) {
        List<Item> itemsForRequest = itemRepo.findAllByRequestId(itemRequest.getId());
        log.info("Размер списка вещей, которые предложены в ответ на запрос {}, составляет: {}",
                itemRequest.getId(), itemsForRequest.size());

        ItemRequestDto itemRequestDto;
        if (itemsForRequest.size() == 0) {
            itemRequestDto = ItemRequestMapper.makeItemRequestDto(itemRequest).orElseThrow(() ->
                    new ItemRequestNotFoundException("ItemRequest is not found"));
        } else {
            itemRequestDto = ItemRequestMapper.makeItemRequestDtoWithItemsList(itemRequest, itemsForRequest)
                    .orElseThrow(() -> new ItemRequestNotFoundException("ItemRequest is not found"));
        }

        return itemRequestDto;
    }

    public void prepareForReturnListDto(Long requesterId) {
        requesterValidate(requesterId);   // проверили, что с тем, кто запрашивает всё в порядке

    }
}
