package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.ItemRequest;

@Data
public class ItemDTO {
    int id;
    String name;
    String description;
    Boolean available;
    int ownerId;
    ItemRequest request;
}
