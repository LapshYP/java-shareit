package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Validation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemDTO {
    int id;
    @NotBlank(groups = Validation.Post.class, message = "must not be blank")
    String name;
    @NotBlank(groups = Validation.Post.class, message = "must not be blank")
    String description;
    @NotNull(groups = Validation.Post.class, message = "must not be null")
    Boolean available;
    int ownerId;
    int requestId;

}
