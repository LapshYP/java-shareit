package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.dto.BookingLastNextItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repossitory.BookingRepoJpa;
import ru.practicum.shareit.item.comment.CommentRepoJpa;
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

    @Mock
    private CommentRepoJpa commentRepoJpa;
    private ModelMapper mapper = new ModelMapper();
    private BookingDto bookingDto;
    private UserDTO userDTO;
    private User user;
    private ItemDTO itemDTO;
    private Item item;
    private Item item2;
    private BookingLastNextItemDto bookingLastNextItemDto;
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
        item = new Item().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1)
                .owner(user)
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

        bookingLastNextItemDto = new BookingLastNextItemDto().builder()
                .id(1)
                .startTime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .endTime(LocalDateTime.of(2024, 7, 9, 13, 56))
                .bookerId(1)
                .status(Status.WAITING)
                .build();


        bookingService = new BookingServiceImpl(bookingRepoJpa, itemRepoJpa,
                userRepoJpa);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void makeBookingService() {
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
    void getByBookerService() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);
        when(bookingRepoJpa.findById(anyInt()))
                .thenReturn(Optional.of(booking));
        BookingForResponse bookerService = bookingService.getByBookerService(1, 1);

        assertEquals(1, bookerService.getId());
    }

    @Test
    void getAllForBookerService() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);

        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));

        when(bookingRepoJpa.findAllByBookerOrderByStartDesc(any(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingForResponse> expectedResult = List.of(mapper.map(booking, BookingForResponse.class));

        assertEquals(expectedResult, bookingService.getAllForBookerService("ALL", 1, 0, 20));
    }

    @Test
    void getAllForOwnerService() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);

        when(userRepoJpa.findById(1))
                .thenReturn(Optional.of(user));

        when(bookingRepoJpa.getAllForOwner(anyInt(), any()))
                .thenReturn(Collections.singletonList(booking));

        List<BookingForResponse> expectedResult = List.of(mapper.map(booking, BookingForResponse.class));

        assertEquals(expectedResult, bookingService.getAllForOwnerService("ALL", 1, 0, 20));
    }

    @Test
    void updateBooking() {
        Booking booking = mapper.map(bookingForResponse, Booking.class);

        when(bookingRepoJpa.findById(anyInt()))
                .thenReturn(Optional.ofNullable(booking));
        BookingForResponse bookingForResponse1 = bookingService.updateBooking(1, 1,true);

        assertEquals(1, bookingForResponse1.getId());
    }
}