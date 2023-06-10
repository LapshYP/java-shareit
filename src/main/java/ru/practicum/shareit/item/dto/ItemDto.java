package ru.practicum.shareit.item.dto;

/**
 * TODO Sprint add-controllers.
 */

import lombok.Data;
import ru.practicum.shareit.item.model.Owner;
import ru.practicum.shareit.item.model.ItemRequest;

@Data
public class ItemDto {
    int id;
    String name;
    String description;
    Boolean available;
    int ownerId;
    ItemRequest request;

}