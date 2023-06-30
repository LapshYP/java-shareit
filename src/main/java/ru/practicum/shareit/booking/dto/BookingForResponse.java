package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingForResponse {
    private int id;
    @JsonProperty("start")
    private LocalDateTime startTime;
    @JsonProperty("end")
    private LocalDateTime endTime;
   private ItemDTO item;
    private UserDTO booker;
    private Status status;
    private State state;

}