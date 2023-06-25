package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {
    ItemRequestService itemRequestService;
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    //    POST /requests — добавить новый запрос вещи.
//    Основная часть запроса — текст запроса, где пользователь описывает, какая именно вещь ему нужна.
    @PostMapping
    ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                              @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Add new request: {} by user id {} - Started", itemRequestDto, requesterId);
        ItemRequestDto itemRequestDtoFromRepo = itemRequestService.addNewItemRequest(requesterId, itemRequestDto);
        log.info("Add new request: {} - Finished", itemRequestDtoFromRepo);
        return itemRequestDtoFromRepo;
    }

//    GET /requests — получить список своих запросов вместе с данными об ответах на них.
//    Для каждого запроса должны указываться описание, дата и время создания и список ответов в формате:
//    id вещи, название, id владельца. Так в дальнейшем, используя указанные id вещей, можно будет получить
//    подробную информацию о каждой вещи.
//    Запросы должны возвращаться в отсортированном порядке от более новых к более старым.
    @GetMapping
    List<ItemRequestDto> getItemRequests (@RequestHeader("X-Sharer-User-Id") Long requesterId){
        log.info("Get requests for user id: {} - Started", requesterId);
        List<ItemRequestDto> listOfRequestsDto = itemRequestService.getItemRequests(requesterId);
        log.info("Size of founded List of requests is {} - Finished", listOfRequestsDto.size());
        return listOfRequestsDto;
    }
    /*
    GET /requests/all?from={from}&size={size} — получить список запросов, созданных другими пользователями.
    С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли
    бы ответить. Запросы сортируются по дате создания: от более новых к более старым. Результаты должны
    возвращаться постранично. Для этого нужно передать два параметра: from — индекс первого элемента,
    начиная с 0, и size — количество элементов для отображения.
     */

    @GetMapping("/all")
    List<ItemRequestDto> getAllItemRequests
                        (@RequestHeader("X-Sharer-User-Id") Long userId,
                         @RequestParam(required = false, defaultValue = "0") String from,
                         @RequestParam(required = false, defaultValue = "1") String size){

        log.info("Get All requests - Started");
        List<ItemRequestDto> listOfRequestsDto = itemRequestService.getAllItemRequests(userId,
                Integer.parseInt(from), Integer.parseInt(size));
        log.info("Size of founded List of requests is {} - Finished", listOfRequestsDto.size());
        return listOfRequestsDto;
    }

    /*
    GET /requests/{requestId} — получить данные об одном конкретном запросе вместе с данными об ответах на
    него в том же формате, что и в эндпоинте GET /requests. Посмотреть данные об отдельном запросе
    может любой пользователь.
     */
    @GetMapping("/{requestId}")
    ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long requestId){
        log.info("Get request id: {} - Started", requestId);
        ItemRequestDto requestItemDto = itemRequestService.getItemRequest(userId, requestId);
        log.info("Request id {} was found - Finished", requestId);

        return requestItemDto;
    }



}
