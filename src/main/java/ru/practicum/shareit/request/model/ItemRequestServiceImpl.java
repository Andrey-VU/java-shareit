package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestMapperService;
import ru.practicum.shareit.request.repo.ItemRequestRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemRequestServiceImpl implements ItemRequestService{
    ItemRequestMapperService itemRequestMapperService;
    ItemRequestRepository itemRequestRepo;

    @Override
    public ItemRequestDto addNewItemRequest(Long requesterId, ItemRequestDto dto) {
        ItemRequest itemRequestForSave = itemRequestMapperService.prepareForSaveItemRequest(requesterId, dto);
        ItemRequest itemRequest = itemRequestRepo.save(itemRequestForSave);
        return ItemRequestMapper.makeItemRequestDto(itemRequest).orElseThrow(() ->
                new NullPointerException("ItemRequest not found"));
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Long userId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        return null;
    }
}
