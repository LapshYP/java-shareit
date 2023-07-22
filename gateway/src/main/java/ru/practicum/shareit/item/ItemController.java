package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.Validation;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    @Validated(Validation.Post.class)
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @Valid @RequestBody ItemDTO itemDTO) {
        return itemClient.createItem(userId, itemDTO);
    }

    @PatchMapping("/{itemId}")
    @Validated(Validation.Patch.class)
    public ResponseEntity<Object> patch(@RequestHeader("X-Sharer-User-Id") int userId,
                                        @Valid @RequestBody ItemDTO itemDTO,
                                        @PathVariable("itemId") int itemId) {
        if (itemDTO.getName() != null && itemDTO.getName().isBlank()) {
            throw new ValidationException("поле имени не может быть пустым");
        }
        if (itemDTO.getDescription() != null && itemDTO.getDescription().isBlank()) {
            throw new ValidationException("поле описания не может быть пустым");
        }
        return itemClient.updateItem(userId, itemId,itemDTO );
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @Valid @RequestBody CommentDto commentDto,
                                             @PathVariable("itemId") int itemId
    ) {
        return itemClient.addCommentToItem(userId, itemId, commentDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemByUserId(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "20") @Positive Integer size) {

        return itemClient.getItemByUserId(userId, from, size);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @PathVariable int itemId) {
        return itemClient.getByOwnerIdService(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByText(
            @RequestHeader("X-Sharer-User-Id") int userId,
            @RequestParam String text,
            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(defaultValue = "20") @Positive int size) {
        return itemClient.searchItemsByText(userId,text, from, size);
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable int itemId) {
        return itemClient.deleteItem(userId, itemId);
    }


}