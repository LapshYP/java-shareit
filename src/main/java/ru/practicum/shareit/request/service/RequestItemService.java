package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.RequestDto;

import java.util.List;

@Service

public interface RequestItemService {

    RequestDto addItemRequestService(RequestDto requestDto, int userId);

    List<RequestDtoWithRequest> getItemRequestSerivice(int userId);

    List<RequestDtoWithRequest> getItemRequestAllSerivice(int userId, int from, int size);


    RequestDtoWithRequest getRequestById(int userId, int requestId);

}
