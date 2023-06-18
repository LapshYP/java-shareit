package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private ItemMapper itemMapper
            = Mappers.getMapper(ItemMapper.class);

    @GetMapping("/search")
    public List<ItemDTO> searchFilms(@RequestParam(required = false) String text) {

        return itemService
                .searchByParamService(text)
                .stream()
                .map(itemMapper::itemToItemDTO)
                .collect(Collectors.toList());

    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDTO> getItemById(@Valid @PathVariable int itemDtoId, @RequestHeader("X-Sharer-User-Id") int userId) {
        Item item = itemService.getByIdService(itemDtoId, userId);
        ItemDTO itemDTO = itemMapper.itemToItemDTO(item);
        return new ResponseEntity<>(itemDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getItemByUserId(@RequestHeader("X-Sharer-User-Id") int userId) {

        return new ResponseEntity<>(itemService.getByUserIdService(userId)
                .stream()
                .map(itemMapper::itemToItemDTO)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO, @RequestHeader("X-Sharer-User-Id") int userId) {
       Item item = itemMapper.itemDTOToItem(itemDTO);
       Item createdItem = itemService.createService(item, userId);
       ItemDTO createdItemDTO = itemMapper.itemToItemDTO(createdItem);
        return new ResponseEntity<>(createdItemDTO, HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDTO> updateItem(@RequestBody ItemDTO itemDTO, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int userId) {
        Item item = itemMapper.itemDTOToItem(itemDTO);
        Item updatedItem = itemService.updateService(item, itemId, userId);
        ItemDTO updatedItemDTO = itemMapper.itemToItemDTO(updatedItem);
        return new ResponseEntity<>(updatedItemDTO, HttpStatus.OK);
    }

}
