package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/search")
    public List<ItemDTO> searchItemsByText(@RequestParam(required = false) String text,
                                           @RequestParam(name = "from", defaultValue = "0") int from,
                                           @RequestParam(name = "size", defaultValue = "20") int size) {
        return itemService.searchByParamService(text, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemLastNextDTO> getItemById(@PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.getByOwnerIdService(itemId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemLastNextDTO>> getItemByUserId(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                                                 @RequestParam(name = "size", defaultValue = "20") int size) {
        return new ResponseEntity<>(itemService.getByBookerIdService(userId, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.createService(itemDTO, userId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDTO> updateItem(@RequestBody ItemDTO itemDTO, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.updateService(itemDTO, itemId, userId), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addCommentToItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                       @PathVariable int itemId, @RequestBody CommentDto commentDto) {
        return itemService.addComment(userId, itemId, commentDto);
    }
}
