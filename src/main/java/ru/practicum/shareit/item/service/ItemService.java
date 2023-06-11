package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemService {
    Item createService(Item item, int userId);

    Item updateService(Item item, int itemId, int userId);

    Item getByIdService(int itemDtoId, int userId);

    List<Item> getByUserIdService(int userId);

    List<Item> searchByParamService(String text);

}
