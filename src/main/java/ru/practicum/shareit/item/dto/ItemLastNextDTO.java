package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemLastNextDTO {
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
  BookingLastNextItemDto lastBooking;
  BookingLastNextItemDto nextBooking;
 //   @JsonProperty("comments")
  List<CommentDto> comments;
}
