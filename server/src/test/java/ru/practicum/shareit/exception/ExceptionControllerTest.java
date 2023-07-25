package ru.practicum.shareit.exception;

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ExceptionControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;

    private UserDTO userDto;
    private ItemDTO itemDTO;
    private BookingForResponse bookingForResponse;
    private BookingDto bookingDto;

    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDTO().builder()
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
                .booker(userDto)
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

    @SneakyThrows
    @Test
    void getAllForUserUnsupportedStatusException() {
        when(bookingService.getAllForBookerService(anyString(), anyInt(), anyInt(), anyInt()))
                .thenThrow(UnsupportedStatusException.class);

        mockMvc.perform(post("/bookings")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(500));
    }


    @SneakyThrows
    @Test
    void getNotFoundException() {
        when(bookingService.makeBookingService(any(), anyInt()))
                .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/{bookingId}", 12345)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8.name())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }
//    @SneakyThrows
//    @Test
//    void createUserConstraintViolationException() {
//        when(userService.createUserSerivce(any()))
//                .thenThrow(ConstraintViolationException.class);
//
//        mockMvc.perform(post("/users")
//                        .content(objectMapper.writeValueAsString(userDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().is(400));
//    }

    //    @SneakyThrows
//    @Test
//    void createUserJdbcSQLIntegrityConstraintViolationException() {
//        when(userService.createUserSerivce(any()))
//                .thenThrow(JdbcSQLIntegrityConstraintViolationException.class);
//
//        mockMvc.perform(post("/users")
//                        .content(objectMapper.writeValueAsString(userDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(status().is(409));
//    }

}