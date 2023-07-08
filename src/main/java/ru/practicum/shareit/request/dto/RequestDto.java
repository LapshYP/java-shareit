package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestDto {

//    int id;
//    String description;
//    @JsonProperty("created")
//    LocalDateTime createdtime;
//    ItemDTO itemDTO;

    int id;

    String description;

    UserDTO requestor;
    @JsonProperty("created")
    LocalDateTime createdtime;

   List<ItemDTO> items = new ArrayList<>();
}
