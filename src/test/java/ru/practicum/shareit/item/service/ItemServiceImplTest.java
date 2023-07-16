package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repossitory.BookingRepoJpa;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepoJpa;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
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

    private Comment comment;
    private Booking booking;
    private Item item;

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
                .build();
        comment = new Comment().builder()
                .id(1L)
                .content("Комментарий")
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        booking = new Booking().builder()
                .id(1)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();

        itemService = new ItemServiceImpl(itemRepoJpa, userRepoJpa, commentRepoJpa,
                bookingRepoJpa);
    }

    @AfterEach
    void tearDown() {
        itemRepoJpa.deleteAll();

    }

    @Test
    void createServiceTest() {
        when(itemRepoJpa.save(any()))
                .thenReturn(item);
        when(userRepoJpa.findById(any()))
                .thenReturn(Optional.ofNullable(user));

        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);

        assertEquals(itemDTO1.getName(), itemService.createService(itemDTO1, user.getId()).getName());

    }

    @Test
    void createItemWithEmptyNameTest() {
        item.setName("");
        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);
        var exception = assertThrows(
                ConstraintViolationException.class,
                () -> itemService.createService(itemDTO1, 999));

        assertEquals("name: не должно быть пустым", exception.getMessage());
    }

    @Test
    void createItemWithEmptyDesctriptionTest() {
        item.setDescription("");
        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);
        var exception = assertThrows(
                ConstraintViolationException.class,
                () -> itemService.createService(itemDTO1, 999));

        assertEquals("description: не должно быть пустым", exception.getMessage());
    }

    @Test
    void createItemWithEmptyAvailableTest() {
        item.setAvailable(null);
        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);
        var exception = assertThrows(
                ConstraintViolationException.class,
                () -> itemService.createService(itemDTO1, 999));

        assertEquals("available: не должно равняться null", exception.getMessage());
    }

    @Test
    void createItemWithWrongUserIDTest() {
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id=77 не найден"));
        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);
        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.createService(itemDTO1, 77));

        assertEquals("404 NOT_FOUND \"Пользователь с id=77 не найден\"", exception.getMessage());
    }

    @Test
    void updateServiceTest() {
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
        ItemDTO updatedItem = itemService.updateService(itemDTO1, 1, 1);
        assertEquals(updatedItem, itemDTO1);

    }

    @Test
    void updateItemWithWrongUserIdTest() {
        when(itemRepoJpa.findById(any()))
                .thenReturn(Optional.ofNullable(item));
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id=77 не найден"));
        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);

        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.updateService(itemDTO1, 1, 77));

        assertEquals("404 NOT_FOUND \"Пользователь с id=77 не найден\"", exception.getMessage());

    }

    @Test
    void updateItemWithWrongItemIdTest() {


        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);

        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.updateService(itemDTO1, 1, 1));

        assertEquals("404 NOT_FOUND \"Вещь c id = '1' не существует\"", exception.getMessage());

    }

    @Test
    void searchByParamServiceTest() {
        when(itemRepoJpa.searchByParam("щётка"))
                .thenReturn(List.of(item));
        ItemDTO itemDTO1 = mapper.map(item, ItemDTO.class);

        assertEquals(List.of(itemDTO1), itemService.searchByParamService("ЩёТка"));

    }

    @Test
    void addCommentTest() {
        booking.setBooker(user);
        item.setBookings(Collections.singletonList(booking));
        when(userRepoJpa.findById(1))
                .thenReturn(Optional.ofNullable(user));
        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.ofNullable(item));

        when(commentRepoJpa.save(any()))
                .thenReturn(comment);
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        CommentDto commentDto1 = itemService.addComment(1, 1, commentDto);


        assertEquals(commentDto1, commentDto);
    }

    @Test
    void addCommentFromWrongItemTest() {

        CommentDto commentDto = mapper.map(comment, CommentDto.class);

        var exception = assertThrows(
                NotFoundException.class,
                () -> itemService.addComment(1, 1, commentDto));

        assertEquals("404 NOT_FOUND \"комментарий  к вещи с id = '1' пользователем с id = 1 ; нет информации о пользователе.\"", exception.getMessage());

    }

    @Test
    void addEmptyCommentTest() {

        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        commentDto.setContent("");

        var exception = assertThrows(
                BadRequestException.class,
                () -> itemService.addComment(1, 1, commentDto));

        assertEquals("400 BAD_REQUEST \"Комментарий не должен быть пустым\"", exception.getMessage());

    }

}