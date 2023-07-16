package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.request.dto.RequestDtoWithRequest;
import ru.practicum.shareit.request.model.RequestDto;
import ru.practicum.shareit.request.service.RequestItemService;
import ru.practicum.shareit.user.dto.UserDTO;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ItemRequestControllerIT {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RequestItemService requestItemService;

    private RequestDto requestDto;
    private RequestDtoWithRequest requestDtoWithRequest;
    private UserDTO userDTO;

    private ItemDTO itemDTO;


    @BeforeEach
    private void setUp() {
        userDTO = new UserDTO().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();


        requestDto = new RequestDto().builder()
                .id(1)
                .createdtime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(userDTO)
                .items(new ArrayList<>())
                .build();

        requestDtoWithRequest = new RequestDtoWithRequest().builder()
                .id(1)
                .createdtime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(userDTO)
                .items(new ArrayList<>())
                .build();

    }

    @Test
    @SneakyThrows
    void addItemRequestTest() {


        when(requestItemService.addItemRequestService(any(), anyInt()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.requestor.id", is(requestDto.getRequestor().getId())))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void requestsGetTest() {
        when(requestItemService.getItemRequestSerivice(anyInt()))
                .thenReturn(List.of(requestDtoWithRequest));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestor.id", is(requestDto.getRequestor().getId())))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())));


    }

    @Test
    @SneakyThrows
    void requestsAllGetTest() {
        when(requestItemService.getItemRequestAllSerivice(1, 0, 20))
                .thenReturn(List.of(requestDtoWithRequest));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].requestor.id", is(requestDto.getRequestor().getId())))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void getRequestByIdTest() {
        when(requestItemService.getRequestById(anyInt(), anyInt()))
                .thenReturn(requestDtoWithRequest);

        mockMvc.perform(get("/requests/{requestId}", requestDtoWithRequest.getId())
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requestor.id", is(requestDto.getRequestor().getId())))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }
}