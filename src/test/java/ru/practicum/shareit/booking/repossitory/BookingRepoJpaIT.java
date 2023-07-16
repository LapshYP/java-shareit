package ru.practicum.shareit.booking.repossitory;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingRepoJpaIT {
    @Autowired
    private BookingRepoJpa bookingRepoJpa;
    @Autowired
    private UserRepoJpa userRepoJpa;
    @Autowired
    private ItemRepoJpa itemRepoJpa;

    @Autowired
    private TestEntityManager em;

    private User booker1;
    private User booker2;
    private Item item1;
    private Item item2;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;
    private Booking booking4;


    @BeforeEach
    void setUp() {
        booker1 = userRepoJpa.save(new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());

        booker2 = userRepoJpa.save(new User().builder()
                .id(2)
                .name("Petr")
                .email("petr@mail.ru")
                .build());

        item1 = itemRepoJpa.save(new Item().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1)
                .owner(booker1)
                .build());
        item2 = itemRepoJpa.save(new Item().builder()
                .id(2)
                .name("Щётка для ванны")
                .description("Стандартная щётка для ванны")
                .available(true)
                .request(1)
                .owner(booker2)
                .build());

        booking1 = bookingRepoJpa.save(new Booking().builder()
                .id(1)
                .start(LocalDateTime.of(2023, 7, 9, 13, 56))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .booker(booker1)
                .item(item1)
                .status(Status.APPROVED)
                .build());
        booking2 = bookingRepoJpa.save(new Booking().builder()
                .id(2)
                .start(LocalDateTime.of(2024, 7, 9, 13, 56))
                .end(LocalDateTime.of(2025, 7, 9, 13, 56))
                .booker(booker2)
                .item(item2)
                .build());

        booking3 = bookingRepoJpa.save(new Booking().builder()
                .id(3)
                .start(LocalDateTime.of(2023, 7, 9, 13, 56))
                .end(LocalDateTime.of(2024, 7, 9, 13, 56))
                .booker(booker2)
                .item(item2)
                .status(Status.WAITING)
                .build());

        booking4 = bookingRepoJpa.save(new Booking().builder()
                .id(4)
                .start(LocalDateTime.of(2022, 7, 9, 13, 56))
                .end(LocalDateTime.of(2023, 7, 9, 13, 56))
                .booker(booker2)
                .item(item2)
                .status(Status.REJECTED)
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepoJpa.deleteAll();
        itemRepoJpa.deleteAll();
        bookingRepoJpa.deleteAll();

    }

    @Test
    public void contextLoads() {
        assertNotNull(em);
    }

    @Test
    @Transactional
    void getAllForOwnerTest() {
        List<Booking> bookingList = bookingRepoJpa.getAllForOwner(booker1.getId(), null);

        assertEquals(bookingList.get(0).getId(), booking1.getId());
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2023, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2024, 7, 9, 13, 56));


    }

    @Test
    @Transactional
    void findAllByBookerAndStartIsAfterOrderByStartDescTest() {
        User booker = userRepoJpa.findById(booker2.getId()).get();
        List<Booking> bookingList = bookingRepoJpa.findAllByBookerAndStartIsAfterOrderByStartDesc(booker, LocalDateTime.now(), paging);

        assertEquals(bookingList.get(0).getId(), booking2.getId());
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2024, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2025, 7, 9, 13, 56));

    }

    @Test
    @Transactional
    void findAllByBookerOrderByStartDescTest() {

        List<Booking> bookingList = bookingRepoJpa.findAllByBookerOrderByStartDesc(booker1, null);

        assertEquals(bookingList.get(0).getItem().getName(), "Щётка для обуви");
        assertEquals(bookingList.get(0).getBooker().getName(), "Ivan");

    }

    @Test
    @Transactional
    void findAllByOwnerAndStartIsAfterOrderByStartDescTest() {
        List<Booking> bookingList = bookingRepoJpa.findAllByOwnerAndStartIsAfterOrderByStartDesc(booker2.getId(), LocalDateTime.now(), paging);

        assertEquals(bookingList.get(0).getId(), booking2.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Petr");
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2024, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2025, 7, 9, 13, 56));

    }

    @Test
    @Transactional
    void findAllBookingsForBookerWithStartAndEndTest() {

        LocalDateTime localDateTime = LocalDateTime.now();
        List<Booking> bookingList = bookingRepoJpa.findAllBookingsForBookerWithStartAndEnd(
                booker1, localDateTime, localDateTime, paging);


        assertEquals(bookingList.get(0).getId(), booking1.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Ivan");
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2023, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2024, 7, 9, 13, 56));
    }

    @Test
    @Transactional
    void findAllByBookerAndStatusEqualsOrderByStartDescTest() {

        List<Booking> bookingList = bookingRepoJpa
                .findAllByBookerAndStatusEqualsOrderByStartDesc(booker1, Status.APPROVED, paging);


        assertEquals(bookingList.get(0).getId(), booking1.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Ivan");
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2023, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2024, 7, 9, 13, 56));
    }

    @Test
    @Transactional
    void findAllByOwnerAndStatusEqualsOrderByStartDescTest() {

        List<Booking> bookingList = bookingRepoJpa.findAllByOwnerAndStatusEqualsOrderByStartDesc(booker2.getId(), Status.WAITING, paging);


        assertEquals(bookingList.get(0).getId(), booking3.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Petr");
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2023, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2024, 7, 9, 13, 56));
    }

    @Test
    @Transactional
    void findAllBookingsForOwnerWithStartAndEndTest() {
        User owener = booker2;
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Booking> bookingList = bookingRepoJpa.findAllBookingsForOwnerWithStartAndEnd(owener, localDateTime, localDateTime, paging);

        assertEquals(bookingList.get(0).getId(), booking3.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Petr");
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2023, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2024, 7, 9, 13, 56));
    }

    @Test
    @Transactional
    void findAllByOwnerAndEndIsBeforeOrderByStartDescTest() {
        User owener = booker2;
        LocalDateTime localDateTime = LocalDateTime.now();
        List<Booking> bookingList = bookingRepoJpa.findAllByOwnerAndEndIsBeforeOrderByStartDesc(owener, localDateTime, paging);


        assertEquals(bookingList.get(0).getId(), booking4.getId());
        assertEquals(bookingList.get(0).getBooker().getName(), "Petr");
        assertEquals(bookingList.get(0).getStart(), LocalDateTime.of(2022, 7, 9, 13, 56));
        assertEquals(bookingList.get(0).getEnd(), LocalDateTime.of(2023, 7, 9, 13, 56));

    }

    @Test
    @Transactional
    void findAllByBookerAndEndIsBeforeOrderByStartDescTest() {

        List<Booking> bookingList = bookingRepoJpa
                .findAllByBookerAndEndIsBeforeOrderByStartDesc(booker1, LocalDateTime.now(), paging);

        assertEquals(bookingList.size(), 0);
    }


}