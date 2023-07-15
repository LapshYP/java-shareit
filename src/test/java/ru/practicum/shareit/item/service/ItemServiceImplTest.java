package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.configurationprocessor.metadata.ItemDeprecation;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.booking.repossitory.BookingRepoJpa;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.CommentRepoJpa;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private ItemRepoJpa itemRepoJpa;

    @Mock
    private UserRepoJpa userRepoJpa;

    @Mock
    private BookingRepoJpa bookingRepoJpa;

    @Mock
    private CommentRepoJpa commentRepoJpa;

    private ModelMapper mapper = new ModelMapper();
    private ItemService itemService;
    private User user;
    private UserDTO userDTO;
    private ItemDTO itemDTO;
    private Item item;

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
        item = new Item().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1)
                .owner(user)
                .build();

        itemService = new ItemServiceImpl(itemRepoJpa, userRepoJpa, commentRepoJpa,
                bookingRepoJpa);
    }

    @AfterEach
    void tearDown() {
        itemRepoJpa.deleteAll();

    }

    @Test
    void createService() {
        when(itemRepoJpa.save(any()))
                .thenReturn(item);
        when(userRepoJpa.findById(any()))
                .thenReturn(Optional.ofNullable(user));

        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);

        assertEquals(itemDTO1.getName(), itemService.createService(itemDTO1, user.getId()).getName());

    }

    @Test
    void createItemWithEmptyName() {
        item.setName("");
        ItemDTO itemDTO1  = mapper.map(item, ItemDTO.class);
        var exception =  assertThrows(
                ConstraintViolationException.class,
                () -> itemService.createService(itemDTO1, 999));

        assertEquals("name: не должно быть пустым", exception.getMessage());
    }
    @Test
    void createItemWithEmptyDesctription() {
        item.setDescription("");
        ItemDTO itemDTO1  = mapper.map(item, ItemDTO.class);
        var exception =  assertThrows(
                ConstraintViolationException.class,
                () -> itemService.createService(itemDTO1, 999));

        assertEquals("description: не должно быть пустым", exception.getMessage());
    }
    @Test
    void createItemWithEmptyAvailable() {
        item.setAvailable(null);
        ItemDTO itemDTO1  = mapper.map(item, ItemDTO.class);
        var exception =  assertThrows(
                ConstraintViolationException.class,
                () -> itemService.createService(itemDTO1, 999));

        assertEquals("available: не должно равняться null", exception.getMessage());
    }
    @Test
    void createItemWithWrongUserID() {
       when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND,"Пользователь с id=77 не найден"));
        ItemDTO itemDTO1  = mapper.map(item, ItemDTO.class);
        var exception =  assertThrows(
                NotFoundException.class,
                () -> itemService.createService(itemDTO1, 77));

        assertEquals("404 NOT_FOUND \"Пользователь с id=77 не найден\"", exception.getMessage());
    }
    @Test
    void updateService() {
        when(itemRepoJpa.findById(any()))
                .thenReturn(Optional.ofNullable(item));

        when(userRepoJpa.findById(any()))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepoJpa.save(any()))
                .thenReturn(item);

       ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);
//        Item  item  = mapper.map(itemDTO, Item .class);
          itemDTO1.setName("Щётка для обуви updated");
          itemDTO1.setDescription("Стандартная щётка для обуви updated");
        ItemDTO updatedItem = itemService.updateService( itemDTO1,1,1);
        assertEquals(updatedItem, itemDTO1);

    }
    @Test
    void updateItemWithWrongUserId() {
        when(itemRepoJpa.findById(any()))
                .thenReturn(Optional.ofNullable(item));
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND,"Пользователь с id=77 не найден"));
        ItemDTO itemDTO1  = mapper.map(item, ItemDTO.class);

        var exception =  assertThrows(
                NotFoundException.class,
                () -> itemService.updateService( itemDTO1,1,77));

        assertEquals("404 NOT_FOUND \"Пользователь с id=77 не найден\"", exception.getMessage());

    }
    @Test
    void updateItemWithWrongItemId() {


      ItemDTO itemDTO1  = mapper.map(item, ItemDTO.class);

        var exception =  assertThrows(
                NotFoundException.class,
                () -> itemService.updateService( itemDTO1,1,1));

        assertEquals("404 NOT_FOUND \"Вещь c id = '1' не существует\"", exception.getMessage());

    }
    @Test
    void getByOwnerIdService() {

    }

    @Test
    void getByBookerIdService() {
    }

    @Test
    void searchByParamService() {
    }

    @Test
    void addComment() {
    }
}