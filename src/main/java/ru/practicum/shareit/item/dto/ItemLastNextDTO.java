package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingLastNextItemDto;
import ru.practicum.shareit.item.comment.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemLastNextDTO {
    private   int id;
    @NotBlank
    private  String name;
    @NotBlank
    @NotNull
    private  String description;
    @NotNull
    private  Boolean available;
    private  int ownerId;
    private  int request;
    private BookingLastNextItemDto lastBooking;
    private BookingLastNextItemDto nextBooking;
    @JsonProperty("comments")
    private List<CommentDto> comments;
}
