package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepoJpaIT {
    @Autowired
    private UserRepoJpa userRepoJpa;
    @Autowired
    private ItemRepoJpa itemRepoJpa;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = userRepoJpa.save(new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());

        item = itemRepoJpa.save(Item.builder()
                .id(1)
                .name("Щётка для обуви")
                .description("Стандартная щётка для обуви")
                .available(true)
                .request(1)
                .owner(user)
                .build());
    }

    @AfterEach
    void tearDown() {
        itemRepoJpa.deleteAll();
        userRepoJpa.deleteAll();
    }

    @Test
    void findAllByOwnerOrderByIdTest() {
        User user = userRepoJpa.findById(1).get();
        List<Item> items = itemRepoJpa.findAllByOwner(user);

        assertEquals(items.get(0).getId(), item.getId());
        assertEquals(items.get(0).getName(), "Щётка для обуви");
        assertEquals(items.get(0).getDescription(), "Стандартная щётка для обуви");

    }

    @Test
    void searchByParamTest() {
        List<Item> itemList = itemRepoJpa.searchByParam("Щётка".toLowerCase());

        assertEquals(itemList.get(0).getId(), item.getId());
        assertEquals(itemList.get(0).getName(), "Щётка для обуви");
        assertEquals(itemList.get(0).getDescription(), "Стандартная щётка для обуви");
    }
}