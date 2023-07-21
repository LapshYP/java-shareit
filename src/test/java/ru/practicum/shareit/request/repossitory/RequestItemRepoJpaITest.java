package ru.practicum.shareit.request.repossitory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class RequestItemRepoJpaITest {

    @Autowired
    private RequestItemRepoJpa requestItemRepoJpa;
    @Autowired
    private UserRepoJpa userRepoJpa;
    @Autowired
    private ItemRepoJpa itemRepoJpa;
    private Request request;
    private User user;

    @BeforeEach
    private void setUp() {
        user = userRepoJpa.save(new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());

        Item item = itemRepoJpa.save(new Item().builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1)
                .owner(user)
                .build());


        request = requestItemRepoJpa.save(new Request().builder()
                .id(1)
                .createdtime(LocalDateTime.of(2023, 7, 9, 13, 56))
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(user)
                .items(Collections.singletonList(item))
                .build());
        System.out.println(request);
    }


    @Test
    void findAllByRequestorIdTest() {
        List<Request> requestList = requestItemRepoJpa.findAllByRequestor_Id(1);
        assertNotNull(requestList);
    }

    @Test
    void findByOwnerIdTest() {
        List<Request> requestList = requestItemRepoJpa.findByOwnerId(0, null);

        assertEquals(requestList.get(0).getId(), request.getId());
        assertEquals(requestList.get(0).getDescription(), "Хотел бы воспользоваться щёткой для обуви");
        assertEquals(requestList.get(0).getCreatedtime(), LocalDateTime.of(2023, 7, 9, 13, 56));
        assertEquals(requestList.get(0).getRequestor().getId(), user.getId());
        assertEquals(requestList.get(0).getRequestor().getName(), "Ivan");
        assertEquals(requestList.get(0).getRequestor().getEmail(), "ivan@mail.ru");

    }

    @AfterEach
    private void tearDown() {
        requestItemRepoJpa.deleteAll();
    }
}