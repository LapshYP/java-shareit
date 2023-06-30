package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
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
    int request;
}
