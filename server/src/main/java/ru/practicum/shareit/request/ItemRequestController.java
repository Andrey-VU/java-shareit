package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                              @RequestBody ItemRequestDto itemRequestDto) {
        log.info("SERVER: Add new request: {} by user id {} - Started", itemRequestDto, requesterId);
        ItemRequestDto itemRequestDtoFromRepo = itemRequestService.addNewItemRequest(requesterId, itemRequestDto);
        log.info("SERVER: Add new request: {} - Finished", itemRequestDtoFromRepo);
        return itemRequestDtoFromRepo;
    }

    @GetMapping
    List<ItemRequestDto> getItemRequests(@RequestHeader("X-Sharer-User-Id") Long requesterId) {
        log.info("SERVER: Get requests for user id: {} - Started", requesterId);
        List<ItemRequestDto> listOfRequestsDto = itemRequestService.getItemRequests(requesterId);
        log.info("SERVER: Size of founded List of requests is {} - Finished", listOfRequestsDto.size());
        return listOfRequestsDto;
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                            @RequestParam(required = false, defaultValue = "20") @Min(1) Integer size) {

        log.info("SERVER: Get All requests - Started");
        List<ItemRequestDto> listOfRequestsDto = itemRequestService.getAllItemRequests(userId, from, size);
        log.info("SERVER: Size of founded List of requests is {} - Finished", listOfRequestsDto.size());
        return listOfRequestsDto;
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long requestId) {
        log.info("SERVER: Get request id: {} - Started", requestId);
        ItemRequestDto requestItemDto = itemRequestService.getItemRequest(userId, requestId);
        log.info("SERVER: Request id {} was found - Finished", requestId);

        return requestItemDto;
    }
}
