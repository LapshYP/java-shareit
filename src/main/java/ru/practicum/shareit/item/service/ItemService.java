package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;

import java.util.List;

@Service
public interface ItemService {
    ItemDTO createService(ItemDTO itemDTO, int userId);

    ItemDTO updateService(ItemDTO itemDTO, int itemId, int userId);

    ItemLastNextDTO getByIdService(int itemId, int userId);

    List<ItemLastNextDTO> getByUserIdService(int userId);

    List<ItemDTO> searchByParamService(String text);

    CommentDto addComment(int userId, int itemId, CommentDto commentDto);

}
