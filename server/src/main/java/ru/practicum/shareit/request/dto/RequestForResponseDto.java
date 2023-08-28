package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestForResponseDto {

    int id;

    String description;

    User requestor;

    LocalDateTime createdtime;

    List<ItemLastNextDTO> items;
}
