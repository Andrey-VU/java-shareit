package ru.practicum.shareit.request.model;

import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto addNewItemRequest(Long requesterId, ItemRequestDto dto);

    List<ItemRequestDto> getItemRequests(Long userId);

    List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size);

    ItemRequestDto getItemRequest(Long userId, Long requestId);
}
