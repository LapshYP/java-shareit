package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.RequestDto;
import ru.practicum.shareit.request.service.RequestItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor

public class ItemRequestController {
    private final RequestItemService requestItemService;

    @PostMapping
    RequestDto addItemRequest(@Valid @RequestBody(required = false) RequestDto requestDto,
                              @RequestHeader("X-Sharer-User-Id") int userId) {
        return requestItemService.addItemRequestService(requestDto, userId);
    }

    @GetMapping
    List<RequestDtoWithRequest> requestsGet(@RequestHeader("X-Sharer-User-Id") int userId) {

        return requestItemService.getItemRequestSerivice(userId);
    }
    @GetMapping(path = "/all")
    List<RequestDtoWithRequest> requestsAllGet (@RequestHeader("X-Sharer-User-Id") int userId,
                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                     @RequestParam(name = "size", defaultValue = "20") Integer size) {

        return requestItemService.getItemRequestAllSerivice(userId,from,size);
    }

    @GetMapping("{requestId}")
    public RequestDtoWithRequest getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @PathVariable int requestId) {
         return requestItemService.getRequestById(userId, requestId);
    }

}
