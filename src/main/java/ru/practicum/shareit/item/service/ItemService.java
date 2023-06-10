package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

@Service
public interface ItemService {
    ItemDto itemCreateService(ItemDto itemDto, int userId);

    ItemDto itemUpdateService(ItemDto itemDto, int itemId, int userId);

    ItemDto itemGetByIdService(int itemDtoId, int userId);

    Collection<ItemDto> itemGetByUserIdService(int userId);

    List<ItemDto> searchItemByParam(String text);

}
