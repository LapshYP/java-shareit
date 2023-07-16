package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repossitory.BookingRepoJpa;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private ItemRepoJpa itemRepoJpa;
    @Mock
    private UserRepoJpa userRepoJpa;
    @Mock
    private BookingRepoJpa bookingRepoJpa;
    private ModelMapper mapper = new ModelMapper();
    private BookingDto bookingDto;
    private BookingDto bookingDto1;
    private BookingDto bookingDto2;
    private BookingDto bookingDto3;
    private UserDTO userDTO;
    private User user;
    private ItemDTO itemDTO;
    private Item item2;
    private BookingForResponse bookingForResponse;
    private BookingService bookingService;

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
        user = new User().builder()
                .id(2)
                .name("Petr")
                .email("petr@mail.ru")
                .build();

        item2 = new Item().builder()
                .id(2)
                .name("Щётка для ванны")
                .description("Стандартная щётка для ванны")
                .available(true)
                .request(2)
                .owner(user)
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
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .itemId(1)
                .booker(user)
                .status(Status.WAITING)
                .build();

        bookingDto1 = new BookingDto().builder()
                .id(1)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().minusMonths(1))
                .itemId(1)
                .booker(user)
                .status(Status.WAITING)
                .build();
        bookingDto2 = new BookingDto().builder()
                .id(1)
                .start(LocalDateTime.now().minusMonths(1))
                .end(LocalDateTime.now().minusMonths(1))
                .itemId(1)
                .booker(user)
                .status(Status.WAITING)
                .build();
        bookingDto3 = new BookingDto().builder()
                .id(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().minusMonths(1))
                .itemId(1)
                .booker(user)
                .status(Status.WAITING)
                .build();

        bookingService = new BookingServiceImpl(bookingRepoJpa, itemRepoJpa,
                userRepoJpa);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void makeBookingServiceTest() {
        Booking booking = mapper.map(bookingDto, Booking.class);
        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));
        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.of(item2));

        when(bookingRepoJpa.save(any()))
                .thenReturn(booking);
        BookingForResponse bookingForResponse1 = bookingService.makeBookingService(bookingDto, 1);

        assertEquals(1, bookingForResponse1.getId());
    }

    @Test
    void makeBookingWithBadBookingTime1Test() {

        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.of(item2));

        var exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.makeBookingService(bookingDto1, 1));
        assertEquals("404 NOT_FOUND \"Ошибка времени создания букинга\"",
                exception.getMessage());
    }

    @Test
    void makeBookingWithBadBookingTime2Test() {

        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.of(item2));

        var exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.makeBookingService(bookingDto2, 1));
        assertEquals("404 NOT_FOUND \"Ошибка времени создания букинга\"",
                exception.getMessage());
    }

    @Test
    void makeBookingWithBadBookingTime3Test() {

        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.of(item2));

        var exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.makeBookingService(bookingDto3, 1));
        assertEquals("404 NOT_FOUND \"Ошибка времени создания букинга\"",
                exception.getMessage());
    }

    @Test
    void makeBookingWithBadBookingTime4Test() {

        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.of(item2));
        bookingDto3.setStart(null);
        bookingDto3.setEnd(null);
        var exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.makeBookingService(bookingDto3, 1));
        assertEquals("404 NOT_FOUND \"Ошибка времени (null) создания букинга\"",
                exception.getMessage());
    }

    @Test
    void makeBookingWithBadBookingTime5Test() {

        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.of(item2));
        bookingDto3.setStart(LocalDateTime.now().minusYears(1));

        var exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.makeBookingService(bookingDto3, 1));
        assertEquals("404 NOT_FOUND \"Ошибка времени создания букинга\"",
                exception.getMessage());
    }

    @Test
    void makeBookingWrongBookerIdTest() {
        when(itemRepoJpa.findById(1))
                .thenReturn(Optional.of(item2));
        when(userRepoJpa.findById(77))
                .thenThrow(new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id=77 не найден"));

        var exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.makeBookingService(bookingDto, 77));

        assertEquals("404 NOT_FOUND \"Пользователь с id=77 не найден\"", exception.getMessage());
    }

    @Test
    void makeBookingWrongItemTest() {

        var exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.makeBookingService(bookingDto, 77));

        assertEquals("404 NOT_FOUND \"Вещь не найдена\"", exception.getMessage());
    }

    @Test
    void getByBookerServiceTest() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);
        when(bookingRepoJpa.findById(anyInt()))
                .thenReturn(Optional.of(booking));
        BookingForResponse bookerService = bookingService.getByBookerService(1, 1);

        assertEquals(1, bookerService.getId());
    }

    @Test
    void getByBookerWrongOwnerIdTest() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);
        when(bookingRepoJpa.findById(anyInt()))
                .thenReturn(Optional.of(booking));

        var exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getByBookerService(1, 77));

        assertEquals("404 NOT_FOUND \"Заказчик  не совпадает с собственником вещи\"", exception.getMessage());
    }


    @Test
    void getAllForBookerServiceTest() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);

        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));

        when(bookingRepoJpa.findAllByBookerOrderByStartDesc(any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingForResponse> expectedResult = List.of(mapper.map(booking, BookingForResponse.class));

        assertEquals(expectedResult, bookingService.getAllForBookerService("ALL", 1, 0, 20));
    }
    @Test
    void getCURRENTForBookerServiceTest() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);

        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));

        when(bookingRepoJpa.findAllByBookerOrderByStartDesc(any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingForResponse> expectedResult = List.of(mapper.map(booking, BookingForResponse.class));

        assertEquals(expectedResult, bookingService.getAllForBookerService("CURRENT", 1, 0, 20));
    }

    @Test
    void getAllForOwnerServiceTest() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);

        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));

        when(bookingRepoJpa.getAllForOwner(anyInt(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingForResponse> expectedResult = List.of(mapper.map(booking, BookingForResponse.class));

        assertEquals(expectedResult, bookingService.getAllForOwnerService("ALL", 1, 0, 20));
    }

    @Test
    void updateBookingTest() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);

        when(bookingRepoJpa.findById(anyInt()))
                .thenReturn(Optional.ofNullable(booking));
        BookingForResponse bookingForResponse1 = bookingService.updateBooking(1, 1, true);

        assertEquals(1, bookingForResponse1.getId());
    }

    @Test
    void updateBookingWithWrongBookerTest() {

        var exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.updateBooking(77, 1, true));
        assertEquals("404 NOT_FOUND \"Букинг с таким айди не найден\"",
                exception.getMessage());
    }


}