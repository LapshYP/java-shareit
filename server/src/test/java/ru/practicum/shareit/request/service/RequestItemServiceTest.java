package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.Request;

import ru.practicum.shareit.request.repossitory.RequestItemRepoJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestItemServiceTest {


    @Mock
    private UserRepoJpa userRepoJpa;

    @Mock
    private RequestItemRepoJpa requestItemRepoJpa;

    private ModelMapper mapper = new ModelMapper();
    @InjectMocks
    private RequestItemServiceImpl requestItemService;
    private User user;
    private Item item;
    private Comment comment;

    private Request request;


    @BeforeEach
    void setUp() {


        user = new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        item = new Item().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1)
                .owner(user)
                .comments(new ArrayList<>())
                .build();
        request = new Request().builder()
                .id(1)
                .createdtime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(user)
                .items(Collections.singletonList(item))
                .build();

        comment = new Comment().builder()
                .id(1L)
                .content("comment")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addItemRequestServiceTest() {
        when(userRepoJpa.findById(1))
                .thenReturn(Optional.ofNullable(user));
        when(requestItemRepoJpa.save(any()))
                .thenReturn(request);
        RequestDto requestDto1 = mapper.map(request, RequestDto.class);
        RequestDto requestDto2 = requestItemService.addItemRequestService(requestDto1, 1);
        assertEquals(requestDto2.getDescription(), requestDto1.getDescription());
        assertEquals(requestDto2.getCreatedtime(), requestDto1.getCreatedtime());
    }

    @Test
    void addItemRequestWtihWrongIdTest() {
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = 77 не найден"));
        var exception = assertThrows(
                NotFoundException.class,
                () -> requestItemService.addItemRequestService(mapper.map(request, RequestDto.class), 77));

        assertEquals("404 NOT_FOUND \"Пользователь с id = 77 не найден\"", exception.getMessage());
    }

    @Test
    void getItemRequestSeriviceTest() {
        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));
        when(requestItemRepoJpa.findAllByRequestor_Id(anyInt()))
                .thenReturn(List.of(request));
        List<RequestDtoWithRequest> requestList = requestItemService.getItemRequestSerivice(1);
        RequestDtoWithRequest requestDto1 = mapper.map(request, RequestDtoWithRequest.class);
        assertEquals(requestList, List.of(requestDto1));
    }


    @Test
    void getItemRequestSeriviceWithWrongUserIdTest() {
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = 77 не найден"));
        var exception = assertThrows(
                NotFoundException.class,
                () -> requestItemService.getItemRequestSerivice(77));

        assertEquals("404 NOT_FOUND \"Пользователь с id = 77 не найден\"", exception.getMessage());

    }

    @Test
    void getItemRequestAllSeriviceTest() {
        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));
        when(requestItemRepoJpa.findByOwnerId(anyInt(), any()))
                .thenReturn(List.of(request));
        List<RequestDtoWithRequest> requestList = requestItemService.getItemRequestAllSerivice(1, 1, 1);
        RequestDtoWithRequest requestDto1 = mapper.map(request, RequestDtoWithRequest.class);
        assertEquals(requestList, List.of(requestDto1));
    }


    @Test
    void getItemRequestAllSeriviceWithWrongRequestIdTest() {

        var exception = assertThrows(
                BadRequestException.class,
                () -> requestItemService.getItemRequestAllSerivice(1, -1, 1));
        assertEquals("400 BAD_REQUEST \"не правильный параметр пагинации\"", exception.getMessage());
    }

    @Test
    void getItemRequestAllSeriviceWithWrongUserIdTest() {
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = 77 не найден"));
        var exception = assertThrows(
                NotFoundException.class,
                () -> requestItemService.getItemRequestAllSerivice(77, 1, 1));

        assertEquals("404 NOT_FOUND \"Пользователь с id = 77 не найден\"", exception.getMessage());

    }

    @Test
    void getRequestByIdTest() {
        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));

        when(requestItemRepoJpa.findById(anyInt()))
                .thenReturn(Optional.ofNullable(request));
        RequestDtoWithRequest requestById = requestItemService.getRequestById(1, 1);

        RequestDtoWithRequest requestDto1 = mapper.map(request, RequestDtoWithRequest.class);

        assertEquals(requestById, requestDto1);
    }

    @Test
    void getRequestByIdWithWrongRequestIdTest() {
        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));

        when(requestItemRepoJpa.findById(66))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Запрос с id = 66 не найден"));

        var exception = assertThrows(
                NotFoundException.class,
                () -> requestItemService.getRequestById(1, 66));
        assertEquals("404 NOT_FOUND \"Запрос с id = 66 не найден\"", exception.getMessage());
    }

    @Test
    void getRequestByWrongUserIdTest() {
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = 77 не найден"));
        var exception = assertThrows(
                NotFoundException.class,
                () -> requestItemService.getRequestById(77, 1));

        assertEquals("404 NOT_FOUND \"Пользователь с id = 77 не найден\"", exception.getMessage());

    }


}