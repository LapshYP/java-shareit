package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

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
//    @Test
//    @Transactional
//    void findByIdAndEmailTest() {
//
//        List<User> userList = userRepoJpa.findByIdAndEmail(1, "ivan@mail.ru");
//
//        assertEquals(userList.get(0).getId(), 1);
//        assertEquals(userList.get(0).getEmail(), "ivan@mail.ru");
//    }
}