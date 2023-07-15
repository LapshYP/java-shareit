package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    private UserRepoJpa userRepoJpa;
    private ModelMapper mapper = new ModelMapper();
    private UserService userService;
    private User user;
    private UserDTO userDTO;
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
                .startTime(LocalDateTime.of(2023, 7, 9, 13, 56, 00))
                .endTime(LocalDateTime.of(2024, 7, 9, 13, 56, 00))
                .item(itemDTO)
                .booker(userDTO)
                .status(Status.WAITING)
                .build();

        userService = new UserServiceImpl(userRepoJpa);
    }

    @AfterEach
    void tearDown() {

        userRepoJpa.deleteAll();

    }

    @Test
    void createUserSerivce() {
        when(userRepoJpa.save(any()))
                .thenReturn(user);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        assertEquals(userDTO.getEmail(), userService.createUserSerivce(userDTO).getEmail());
    }

    @Test
    void createUserSerivceWithBadName() {
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setName("");
        assertThrows(
                ConstraintViolationException.class,
                () -> userService.createUserSerivce(userDTO));
    }

    @Test
    void getAll() {

        when(userRepoJpa.findAll())
                .thenReturn(List.of(user));
        List<UserDTO> expectedResult = List.of(mapper.map(user, UserDTO.class));

        assertEquals(expectedResult, userService.getAll());
    }

    @Test
    void updateUserNameAndEmail() {

        user.setEmail("ivan@mailupdated.ru");
        user.setName("ivanupdated");


        UserDTO userDto = mapper.map(user, UserDTO.class);
        Mockito.when(userRepoJpa.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepoJpa.save(Mockito.any()))
                .thenReturn(user);
        UserDTO content = userService.updateUserService(userDto, userDto.getId());

        assertEquals(content.getEmail(), "ivan@mailupdated.ru");
        assertEquals(content.getName(), "ivanupdated");
    }

    @Test
    void deleteUserServiceWtihBadId() {
        Mockito.when(userRepoJpa.findById(777))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = 777 не найден в базе данных"));
        NotFoundException exception =
                assertThrows(NotFoundException.class,
                        () -> userService.deleteUserService(777));

        assertEquals("404 NOT_FOUND \"Пользователь с id = 777 не найден в базе данных\"", exception.getMessage());
    }

    @Test
    void getUserSerivece() {
    }
}