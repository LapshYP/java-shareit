package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerIT {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;
    private UserDTO userDTO;
    private User user;
    private ItemDTO itemDTO;

    private BookingForResponse bookingForResponse;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();
        user = new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        itemDTO = new ItemDTO().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .requestId(1)
                .ownerId(1)
                .build();

        bookingForResponse = new BookingForResponse().builder()
                .id(1)
                .startTime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .endTime(LocalDateTime.of(2024, 7, 9, 13, 56))
                .item(itemDTO)
                .booker(userDTO)
                .status(Status.WAITING)
                .build();

        bookingDto = new BookingDto().builder()
                .id(1)
                .start(LocalDateTime.of(2023, 7, 9, 13, 56))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .itemId(1)
                .booker(user)
                .status(Status.WAITING)
                .build();


    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    void makeBookingTest() {
        when(bookingService.makeBookingService(any(), anyInt()))
                .thenReturn(bookingForResponse);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))

                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))

                .andExpect(jsonPath("$.end", is(bookingDto.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @SneakyThrows
    @Test
    void setApproveByOwnerCurrentTest() {
        when(bookingService.updateBooking(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(bookingForResponse);

        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        // .content(objectMapper.writeValueAsString(bookingDto))

                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))

                .andExpect(jsonPath("$.end", is(bookingDto.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @SneakyThrows
    @Test
    void getByBookerTest() {

        when(bookingService.getByBookerService(anyInt(), anyInt()))
                .thenReturn(bookingForResponse);

        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId())))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))

                .andExpect(jsonPath("$.end", is(bookingDto.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));

    }

    @SneakyThrows
    @Test
    void getAllForUserTest() {

        when(bookingService.getAllForBookerService(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingForResponse));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId())))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))

                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @SneakyThrows
    @Test
    void getAllForOwnerTest() {

        when(bookingService.getAllForOwnerService(anyString(), anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingForResponse));

        mockMvc.perform(get("/bookings/owner", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId())))
                .andExpect(jsonPath("$[0].start", is(bookingDto.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))

                .andExpect(jsonPath("$[0].end", is(bookingDto.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

}