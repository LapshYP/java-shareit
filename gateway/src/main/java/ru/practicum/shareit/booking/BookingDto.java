package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class BookingDto {
    int id;
    @NotNull(message = "must not be null")
    private int itemId;
    @NotNull(message = "must not be blank")
    private LocalDateTime start;
    @NotNull(message = "must not be null")
    private LocalDateTime end;

}

