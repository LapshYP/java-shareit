package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemLastNextDtoMapper {
    Item LastNextDTOToItem (ItemLastNextDTO itemLastNextDTO);

    ItemLastNextDTO itemToItemLastNextDTO(Item item);
}
