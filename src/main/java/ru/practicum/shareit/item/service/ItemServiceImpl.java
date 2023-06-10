package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    @Override
    public ItemDto itemCreateService(ItemDto itemDto, int userId) {


        return itemRepository.createItemRepo(itemDto, userId);
    }

    @Override
    public ItemDto itemUpdateService(ItemDto itemDto, int itemId, int userId) {
        return itemRepository.updateItemRepo(itemDto,itemId,userId);
    }

    @Override
    public ItemDto itemGetByIdService(int itemDtoId, int userId) {
        return itemRepository.getItemById(itemDtoId,userId);
    }

    @Override
    public Collection<ItemDto> itemGetByUserIdService(int userId) {
        return itemRepository.getItemByUserId(userId);
    }

    @Override
    public List<ItemDto> searchItemByParam(String text) {
        return itemRepository.itemSearchByParamService(text);
    }

}
