package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
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
    public List<Item> searchFilms(@RequestParam(required = false) String text) {
        return itemService.searchByParamService(text);

    }

    @GetMapping("/{itemDtoId}")
    public ResponseEntity<Item> getItemById(@Valid @PathVariable int itemDtoId, @RequestHeader("X-Sharer-User-Id") int userId) {

        return new ResponseEntity<>(itemService.getByIdService(itemDtoId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItemByUserId(@RequestHeader("X-Sharer-User-Id") int userId) {

        return new ResponseEntity<>(itemService.getByUserIdService(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.createService(item, userId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@RequestBody Item item, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.updateService(item, itemId, userId), HttpStatus.OK);
    }

}
