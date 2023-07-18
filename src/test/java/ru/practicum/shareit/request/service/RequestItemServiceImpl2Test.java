package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repossitory.RequestItemRepoJpa;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class RequestItemServiceImpl2Test {

    @Mock
    private UserRepoJpa userRepoJpa;

    @Mock
    private RequestItemRepoJpa requestItemRepoJpa;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private RequestItemServiceImpl requestItemService;

    private User user;
    private Request request;
    private RequestDtoWithRequest expectedRequestDtoWithRequest;

    @BeforeEach
    public void setup() {
        user = new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        request = new Request().builder()
                .id(1)
                .createdtime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(user)
                .items(new ArrayList<>())
                .build();
        expectedRequestDtoWithRequest = new RequestDtoWithRequest().builder()
                .id(1)
                .createdtime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(mapper.map(user, UserDTO.class))
                .items(new ArrayList<>())
                .build();

        when(userRepoJpa.findById(anyInt())).thenReturn(Optional.of(user));
        when(requestItemRepoJpa.findById(anyInt())).thenReturn(Optional.of(request));
        when(mapper.map(request, RequestDtoWithRequest.class)).thenReturn(expectedRequestDtoWithRequest);
    }

    @Test
    public void testGetRequestById() {
        int userId = 1;
        int requestId = 1;

        RequestDtoWithRequest actualRequestDtoWithRequest = requestItemService.getRequestById(userId, requestId);

        assertEquals(expectedRequestDtoWithRequest.getItems(), actualRequestDtoWithRequest.getItems());
        assertEquals(requestId, actualRequestDtoWithRequest.getRequestor().getId());

        verify(userRepoJpa, times(1)).findById(userId);
        verify(requestItemRepoJpa, times(1)).findById(requestId);
        // verify(mapper, times(1)).map(request, RequestDtoWithRequest.class);
    }
}