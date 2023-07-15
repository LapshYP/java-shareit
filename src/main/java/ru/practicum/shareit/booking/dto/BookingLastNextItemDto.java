package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NotNull
    LocalDateTime startTime;
    @JsonProperty("end")
    @NotNull
    LocalDateTime endTime;
    int bookerId;
    Status status;
}
