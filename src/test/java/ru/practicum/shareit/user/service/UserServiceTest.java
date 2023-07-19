package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepoJpa userRepoJpa;
    private ModelMapper mapper = new ModelMapper();
    @InjectMocks
    private UserServiceImpl userService;
    private User user;


    @BeforeEach
    void setUp() {

        user = new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();


    }

    @AfterEach
    void tearDown() {

        userRepoJpa.deleteAll();

    }

    @Test
    void createUserSerivceTest() {
        when(userRepoJpa.save(any()))
                .thenReturn(user);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        UserDTO userSavedDto = userService.createUserSerivce(userDTO);

        assertNotNull(userSavedDto);
        assertEquals(user.getId(), userSavedDto.getId());
    }

    @Test
    void createUserSerivceWithExceptionTest() {

        UserDTO userDTO = mapper.map(user, UserDTO.class);

        Mockito.when(userRepoJpa.save(any()))
                .thenReturn(user)
                .thenThrow(new DubleException("Ошибка при сохранении"));


        UserDTO userSavedDto = userService.createUserSerivce(userDTO);

        assertNotNull(userSavedDto);

        assertThrows(
                DubleException.class,
                () -> userService.createUserSerivce(userDTO)
        );
    }

    @Test
    void createUserSerivceWithBadNameTest() {
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setName("");
        assertThrows(
                ConstraintViolationException.class,
                () -> userService.createUserSerivce(userDTO));
    }

    @Test
    void createUserSerivceWithBadEmailTest() {
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userDTO.setEmail("@");
        assertThrows(
                ConstraintViolationException.class,
                () -> userService.createUserSerivce(userDTO));
    }

    @Test
    void getAllTest() {

        when(userRepoJpa.findAll())
                .thenReturn(List.of(user));
        List<UserDTO> expectedResult = List.of(mapper.map(user, UserDTO.class));

        assertEquals(expectedResult, userService.getAll());
    }

    @Test
    void updateUserNameAndEmailTest() {

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
    void updateUserWithBadNameTest() {

        // user.setEmail("ivan@mailupdated.ru");
        user.setName("");
        Mockito.when(userRepoJpa.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));

        UserDTO userDto = mapper.map(user, UserDTO.class);

        assertThrows(
                ConstraintViolationException.class,
                () -> userService.updateUserService(userDto, userDto.getId()));
    }

    @Test
    void updateUserWithBadEmailTest() {

        user.setEmail("ivan@");

        Mockito.when(userRepoJpa.findById(Mockito.any()))
                .thenReturn(Optional.ofNullable(user));

        UserDTO userDto = mapper.map(user, UserDTO.class);

        assertThrows(
                ConstraintViolationException.class,
                () -> userService.updateUserService(userDto, userDto.getId()));
    }

    @Test
    void deleteUserServiceWtihBadIdTest() {
        Mockito.when(userRepoJpa.findById(777))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = 777 не найден в базе данных"));
        NotFoundException exception =
                assertThrows(NotFoundException.class,
                        () -> userService.deleteUserService(777));

        assertEquals("404 NOT_FOUND \"Пользователь с id = 777 не найден в базе данных\"", exception.getMessage());
    }

    @Test
    void deleteUserServiceTest() {
        when(userRepoJpa.save(any()))
                .thenReturn(user);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        assertEquals(userDTO.getEmail(), userService.createUserSerivce(userDTO).getEmail());

        Mockito.when(userRepoJpa.findById(1))
                .thenReturn(Optional.ofNullable(user));
        userService.deleteUserService(1);

        assertThrows(IndexOutOfBoundsException.class,
                () -> userRepoJpa.findAll().get(0));


    }

    @Test
    void getUserByIdTest() {
        Mockito.when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));
        userService.getUserSerivece(1);
        UserDTO userDTO = mapper.map(user, UserDTO.class);

        assertEquals(userDTO, userService.getUserSerivece(1));
    }
}