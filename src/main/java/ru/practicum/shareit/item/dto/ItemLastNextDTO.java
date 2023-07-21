package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingLastNextItemDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemLastNextDTO)) return false;
        ItemLastNextDTO that = (ItemLastNextDTO) o;
        return getId() == that.getId() && getOwnerId() == that.getOwnerId() && getRequest() == that.getRequest() && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getAvailable(), that.getAvailable()) && Objects.equals(getLastBooking(), that.getLastBooking()) && Objects.equals(getNextBooking(), that.getNextBooking()) && Objects.equals(getComments(), that.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAvailable(), getOwnerId(), getRequest(), getLastBooking(), getNextBooking(), getComments());
    }
}
