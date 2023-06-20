package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserService;

@AllArgsConstructor
@Component
public class ItemRequestMapperService {
    UserService userService;

    public ItemRequest prepareForSaveItemRequest(Long requesterId, ItemRequestDto dto) {
        User requester = UserMapper.makeUserWithId(userService.getUser(requesterId)).orElseThrow(() ->
                new UserNotFoundException("Пользователь не обнаружен"));
        return ItemRequestMapper.makeItemRequest(dto, requester).orElseThrow(() ->
                new NullPointerException("ItemRequest not found"));
    }
}
