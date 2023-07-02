package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestItemService requestItemService;
    @PostMapping
    ItemRequestDto addItemRequest (@Valid @RequestBody(required = false) ItemRequestDto itemRequestDto,
                                   @RequestHeader("X-Sharer-User-Id") int userId){
      return   requestItemService.addItemRequestService (itemRequestDto,userId);
    }
    @GetMapping
    List<ItemRequestDto> requestsGet (@Valid @RequestHeader("X-Sharer-User-Id") int userId) {

        return requestItemService.getItemRequestSerivice(userId);
    }
//    @GetMapping
//    List<ItemRequestDto> requestsGet (@Valid @RequestParam int from, @RequestParam int size, @RequestHeader("X-Sharer-User-Id") int userId) {
//
//        return requestItemService.getItemRequestSerivice(from,size,userId);
//    }


}
