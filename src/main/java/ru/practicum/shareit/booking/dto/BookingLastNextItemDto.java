package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class BookingLastNextItemDto {
    private int id;
    @JsonProperty("start")
    private LocalDateTime startTime;
    @JsonProperty("end")
    private LocalDateTime endTime;
    private int bookerId;
    private Status status;
}
