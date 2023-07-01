package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDTO;

import java.util.List;

@Service
public interface ItemService {
    ItemDTO createService(ItemDTO itemDTO, int userId);

    ItemDTO updateService(ItemDTO itemDTO, int itemId, int userId);

    ItemDTO getByIdService(int itemId, int userId);

    List<ItemDTO> getByUserIdService(int userId);

    List<ItemDTO> searchByParamService(String text);
}
