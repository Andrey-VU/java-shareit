package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

public final class ItemRequestMapper {
    public static Optional<ItemRequest> makeItemRequest(ItemRequestDto dto, User requester) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(requester);
        itemRequest.setDescription(dto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());

        return Optional.of(itemRequest);
    }

    public static Optional<ItemRequestDto> makeItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setRequesterId(itemRequest.getId());
        itemRequestDto.setRequesterId(itemRequest.getRequester().getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());

        return Optional.of(itemRequestDto);
    }
}
