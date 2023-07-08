package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDTO {
    int id;
    String name;
    String description;
    Boolean available;
    int ownerId;
    int requestId;
    // List<Request> requests;

}
