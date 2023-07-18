package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.RequestDto;
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
    public RequestDto addItemRequestService(RequestDto itemRequestDto, int userId) {

        User requestor = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер с таким именем не найден в базе данных"));
        Request request = mapper.map(itemRequestDto, Request.class);
        request.setCreatedtime(LocalDateTime.now());
        request.setRequestor(requestor);
        Request saved = requestItemRepoJpa.save(request);
        RequestDto requestDto = mapper.map(saved, RequestDto.class);
        return requestDto;
    }

    @Override
    public List<RequestDtoWithRequest> getItemRequestSerivice(int userId) {
        User requestor = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер с таким именем не найден в базе данных"));
        List<RequestDtoWithRequest> requestDtoWithRequests =
                requestItemRepoJpa.findAllByRequestor_Id(userId).stream()
                        .map(request -> {
                            return mapper.map(request, RequestDtoWithRequest.class);
                        })
                        .collect(Collectors.toList());
        for (RequestDtoWithRequest withRequest : requestDtoWithRequests) {
            for (ItemDTO item : withRequest.getItems()) {
                item.setRequestId(withRequest.getId());
            }
        }
        return requestDtoWithRequests;
    }

    @Override
    public List<RequestDtoWithRequest> getItemRequestAllSerivice(int userId, int from, int size) {
        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "не правильный параметр пагинации");
        }
        User requestor = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер с таким именем не найден в базе данных"));
        Pageable pageable = PageRequest.of(from / size, size);
        List<Request> byOwnerId = requestItemRepoJpa.findByOwnerId(userId, pageable);
        List<RequestDtoWithRequest> requestDtoWithRequests =
                byOwnerId.stream()
                        .map(request -> {
                            return mapper.map(request, RequestDtoWithRequest.class);
                        })
                        .collect(Collectors.toList());
        for (RequestDtoWithRequest withRequest : requestDtoWithRequests) {
            for (ItemDTO item : withRequest.getItems()) {
                item.setRequestId(withRequest.getId());
            }
        }
        return requestDtoWithRequests;
    }

    @Override
    public RequestDtoWithRequest getRequestById(int userId, int requestId) {
        User requestor = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Юзер с таким именем не найден в базе данных"));
        Request request = requestItemRepoJpa.findById(requestId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Запрос вещи по id не найден"));
        RequestDtoWithRequest requestDtoWithRequest = mapper.map(request, RequestDtoWithRequest.class);
        for (ItemDTO item : requestDtoWithRequest.getItems()) {
            item.setRequestId(requestId);
        }
        return requestDtoWithRequest;
    }
}
