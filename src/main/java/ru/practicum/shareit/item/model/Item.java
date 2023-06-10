package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {
    int id;
    String name;
    String description;
    Boolean available;
    Owner owner;
    ItemRequest request;
}
