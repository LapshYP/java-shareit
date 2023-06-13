package ru.practicum.shareit.item.model;

/**
 * TODO Sprint add-controllers.
 */

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {
    int id;
    @NotBlank
    String name;
    @NotBlank
    @NotNull
    String description;
    @NotNull
    Boolean available;
    int ownerId;
    ItemRequest request;

}