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
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerIT {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private ItemService itemService;

    private ItemDTO itemDTO;
    private ItemLastNextDTO ItemLastNextDTO;
    private CommentDto commentDto;
    private User user;

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

        ItemLastNextDTO = new ItemLastNextDTO().builder()
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
    void searchFilms() {
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
    void getItemById() {
        when(itemService.getByOwnerIdService(anyInt(), anyInt()))
                .thenReturn(ItemLastNextDTO);

        mockMvc.perform(get("/items/{id}", itemDTO.getId())
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(ItemLastNextDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDTO.getName())))
                .andExpect(jsonPath("$.description", is(itemDTO.getDescription())));

    }

    @Test
    @SneakyThrows
    void getItemByUserId() {


        when(itemService.getByBookerIdService(anyInt()))
                .thenReturn(List.of(ItemLastNextDTO));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(ItemLastNextDTO.getName())))
                .andExpect(jsonPath("$[0].description", is(ItemLastNextDTO.getDescription())));

    }

    @SneakyThrows
    @Test
    void createItem() {
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
    void updateItem() {
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

//    @SneakyThrows
//    @Test
//    void addCommentToItem() {
//        when(itemService.addComment(any(), anyInt(), commentDto)).thenReturn(commentDto);
//        String writeValueAsString = objectMapper.writeValueAsString(commentDto);
//        mockMvc.perform(post("/items/{itemId}/comment", 1)
//                        .header("X-Sharer-User-Id", 1)
//                        .content(objectMapper.writeValueAsString(commentDto))
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(commentDto.getId())))
//                .andExpect(jsonPath("$.text", is(commentDto.getContent())));
//    }
}