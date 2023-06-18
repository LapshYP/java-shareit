package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
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
