package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository {

    ItemDto createItemRepo(ItemDto itemDto, int userId);

    ItemDto updateItemRepo(ItemDto itemDto, int itemId, int userId);

    ItemDto getItemById(int itemDtoId, int userId);

    Collection<ItemDto> getItemByUserId(int userId);

    List<ItemDto> itemSearchByParamService(String text);

}
