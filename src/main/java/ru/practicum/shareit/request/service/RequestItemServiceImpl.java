package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemRequestDto;
import ru.practicum.shareit.request.repossitory.RequestItemRepoJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestItemServiceImpl implements RequestItemService {

    private final RequestItemRepoJpa requestItemRepoJpa;
    private final UserRepoJpa userRepoJpa;
    private final ModelMapper mapper = new ModelMapper();

    @Override
    public ItemRequestDto addItemRequestService(ItemRequestDto itemRequestDto, int userId) {
        User requestor = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер с таким именем не найден в базе данных"));
        ItemRequest itemRequest = mapper.map(itemRequestDto, ItemRequest.class);
        itemRequest.setCreatedtime(LocalDateTime.now());
        itemRequest.setRequestor(requestor);
        requestItemRepoJpa.save(itemRequest);
        ItemRequestDto requestDto = mapper.map(itemRequest, ItemRequestDto.class);
        return requestDto;
    }

    @Override
    public List<ItemRequestDto> getItemRequestSerivice(int userId) {
        User requestor = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер с таким именем не найден в базе данных"));


        return  requestItemRepoJpa.findAll()
                .stream()
                .map(itemRequest -> {return mapper.map(itemRequest,ItemRequestDto.class);})
                .collect(Collectors.toList());
    }
//    @Override
//    public List<ItemRequestDto> getItemRequestSerivice(int from, int size, int userId) {
//        User requestor = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер с таким именем не найден в базе данных"));
//        ItemRequest itemRequest = new ItemRequest();
//
//        return  requestItemRepoJpa.findAll();
//    }
}
