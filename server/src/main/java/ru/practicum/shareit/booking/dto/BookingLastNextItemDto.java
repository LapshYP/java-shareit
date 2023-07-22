package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingLastNextItemDto {
    int id;
    @JsonProperty("start")
    LocalDateTime startTime;
    @JsonProperty("end")
    LocalDateTime endTime;
    int bookerId;
    Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingLastNextItemDto)) return false;
        BookingLastNextItemDto that = (BookingLastNextItemDto) o;
        return getId() == that.getId() && getBookerId() == that.getBookerId() && Objects.equals(getStartTime(), that.getStartTime()) && Objects.equals(getEndTime(), that.getEndTime()) && getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getStartTime(), getEndTime(), getBookerId(), getStatus());
    }
}
