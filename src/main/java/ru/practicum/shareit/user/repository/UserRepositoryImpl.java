package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Integer, User> userStorage = new HashMap<>();

    @Override
    public HashMap<Integer, User> getUserStorage() {
        return userStorage;
    }

    @Override
    public List<User> getAllUsers() {
      return new ArrayList<>(userStorage.values());
    }

    @SneakyThrows
    @Override
    public User createUserRepo(User user) {
        userStorage.put(user.getId(), user);
        return user;
    }

    @SneakyThrows
    @Override
    public User getUserById(int id) {
        return userStorage.get(id);
    }

    @SneakyThrows
    @Override
    public User updateUserRepo(User updatedUser, int userId) {
        userStorage.put(userId, updatedUser);
        return updatedUser;
    }

    @Override
    public User deleteUserRepo(int userId) {
        userStorage.remove(userId);
        return getUserById(userId);
    }
}
