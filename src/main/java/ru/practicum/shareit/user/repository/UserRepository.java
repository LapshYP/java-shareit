package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface UserRepository {

    List<User> getAllUsers();

    User createUserRepo(User user);

    User getUserById(int id);

    User updateUserRepo(User user, int userId);

    User deleteUserRepo(int userId);

}
