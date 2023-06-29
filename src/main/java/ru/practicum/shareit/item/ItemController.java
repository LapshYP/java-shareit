package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/search")
    public List<ItemDTO> searchFilms(@RequestParam(required = false) String text) {
        return itemService.searchByParamService(text);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemLastNextDTO> getItemById(@Valid @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.getByOwnerIdService(itemId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemLastNextDTO>> getItemByUserId(@RequestHeader("X-Sharer-User-Id") int userId) {
        return new ResponseEntity<>(itemService.getByBookerIdService(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") int userId) {
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
