package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingLastNextItemDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemLastNextDTO {
    int id;
    String name;
    String description;
    Boolean available;
    int ownerId;
    int request;
    BookingLastNextItemDto lastBooking;
    BookingLastNextItemDto nextBooking;
    List<CommentDto> comments;

}
