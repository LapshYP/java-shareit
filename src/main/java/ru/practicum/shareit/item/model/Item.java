package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
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