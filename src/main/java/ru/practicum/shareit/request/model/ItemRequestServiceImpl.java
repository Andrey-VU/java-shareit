package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestMapperService;
import ru.practicum.shareit.request.repo.ItemRequestRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<ItemRequestDto> getItemRequests(Long requesterId) {
        List<ItemRequest> itemRequests = itemRequestRepo.findAllByRequesterIdOrderByCreatedDesc(requesterId);
        return itemRequests.stream()
                .map(itemRequest -> ItemRequestMapper.makeItemRequestDto(itemRequest)
                        .orElseThrow(() -> new ItemRequestNotFoundException("ItemRequest not found")))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(Long userId, Integer from, Integer size) {

        Page<ItemRequest> answerPage = itemRequestRepo.findAll(PageRequest.of(from, size));
        List<ItemRequest> answerList = answerPage
                .stream()
                .collect(Collectors.toList());
        List<ItemRequestDto> answerDtoList = answerList.stream()
                .map(itemRequest -> ItemRequestMapper.makeItemRequestDto(itemRequest)
                        .orElseThrow(() -> new ItemRequestNotFoundException("ItemRequest not found")))
                .collect(Collectors.toList());

        return answerDtoList;
    }

    @Override
    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        return ItemRequestMapper.makeItemRequestDto(itemRequestRepo.findById(requestId)
                        .orElseThrow(() -> new ItemRequestNotFoundException("Item is not found!")))
                .orElseThrow(() -> new ItemRequestNotFoundException("ItemDto is not found!"));
        }
}
