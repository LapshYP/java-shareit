package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

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
    public List<ItemDto> itemGetByUserIdService(int userId) {
        return itemRepository.getItemByUserId(userId);
    }

    @Override
    public List<ItemDto> searchItemByParam(String text) {
        if (text.isEmpty()) {
            throw new NotFoundException(HttpStatus.BAD_REQUEST,"Текст не должен быть пустым");
        }

        return itemRepository.itemSearchByParamService(text);
    }

}
