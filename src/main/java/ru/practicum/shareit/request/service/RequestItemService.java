package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.model.ItemRequestDto;

import java.util.List;

@Service
public interface RequestItemService {

    ItemRequestDto addItemRequestService(ItemRequestDto itemRequestDto, int userId);

    List<ItemRequestDto> getItemRequestSerivice(int userId);

}
