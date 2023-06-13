package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDTO itemToItemDTO(Item item);

    Item itemDTOToItem(ItemDTO itemDTO);
}