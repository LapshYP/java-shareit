package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class Booking {
    int id;
    LocalDateTime start;
    LocalDateTime end;
    ItemDTO item;
    User booker;
    Status status;
}
