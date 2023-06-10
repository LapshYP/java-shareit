package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/search")
    public List<ItemDto> searchFilms(@RequestParam String text) {
        return itemService.searchItemByParam(text);

    }
    @GetMapping("/{itemDtoId}")
    public ResponseEntity<ItemDto> getItemById (@Valid @PathVariable int itemDtoId, @RequestHeader("X-Sharer-User-Id") int userId){

        return new ResponseEntity<>(itemService.itemGetByIdService(itemDtoId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Collection<ItemDto>> getItemByUserId (@RequestHeader("X-Sharer-User-Id") int userId){

        return new ResponseEntity<>(itemService.itemGetByUserIdService(userId), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.itemCreateService(itemDto, userId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem (@RequestBody ItemDto itemDto, @PathVariable int itemId,@RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.itemUpdateService(itemDto, itemId,userId), HttpStatus.OK);
    }

}
