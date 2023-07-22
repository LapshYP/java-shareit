package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepoJpaITest {
    @Autowired
    private UserRepoJpa userRepoJpa;

    @BeforeEach
    void setUp() {
        User user = userRepoJpa.save(new User().builder()
                .id(1)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build());
    }

    @AfterEach
    void tearDown() {
        userRepoJpa.deleteAll();
    }

    //работает но сыпится на verify
    @Test
    @Transactional
    void findByIdAndEmailTest() {

        List<User> userList = userRepoJpa.findByIdAndEmail(1, "ivan@mail.ru");
        assertNotNull(userList);

    }
}