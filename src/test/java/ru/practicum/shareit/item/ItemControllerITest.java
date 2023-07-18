package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerITest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ItemService itemService;

    private ItemDTO itemDTO;
    private ItemLastNextDTO itemLastNextDTO;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDTO = new ItemDTO().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .requestId(1)
                .ownerId(1)
                .build();

        itemLastNextDTO = new ItemLastNextDTO().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1)
                .ownerId(1)
                .lastBooking(null)
                .nextBooking(null)
                .build();

        commentDto = new CommentDto().builder()
                .id(1L)
                .content("comment")
                .authorName("Petr")
                .created(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    void searchFilmsTest() {
        when(itemService.searchByParamService("name"))
                .thenReturn(List.of(itemDTO));

        mockMvc.perform(get("/items/search?text=name")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDTO.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDTO.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDTO.getAvailable())));
    }

    @Test
    @SneakyThrows
    void getItemByIdTest() {
        when(itemService.getByOwnerIdService(anyInt(), anyInt()))
                .thenReturn(itemLastNextDTO);

        mockMvc.perform(get("/items/{id}", itemDTO.getId())
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemLastNextDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDTO.getName())))
                .andExpect(jsonPath("$.description", is(itemDTO.getDescription())));

    }

    @Test
    @SneakyThrows
    void getItemByUserIdTest() {


        when(itemService.getByBookerIdService(anyInt()))
                .thenReturn(List.of(itemLastNextDTO));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemLastNextDTO.getName())))
                .andExpect(jsonPath("$[0].description", is(itemLastNextDTO.getDescription())));

    }

    @SneakyThrows
    @Test
    void createItemTest() {
        when(itemService.createService(any(), anyInt()))
                .thenReturn(itemDTO);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDTO.getName())))
                .andExpect(jsonPath("$.description", is(itemDTO.getDescription())));
    }

    @SneakyThrows
    @Test
    void updateItemTest() {
        when(itemService.updateService(any(), anyInt(), anyInt()))
                .thenReturn(itemDTO);


        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}