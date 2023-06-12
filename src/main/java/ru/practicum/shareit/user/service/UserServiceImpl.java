package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static int id = 1;

    @SneakyThrows
    @Override
    public User createUserSerivce(User user) {
        if (userRepository.getUserStorage().values().stream().filter(user1 -> user1.getEmail().equals(user.getEmail())).count() > 0) {
            throw new DubleException("Уже существует");
        }
        user.setId(id++);
        userRepository.createUserRepo(user);

        return user;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    @SneakyThrows
    @Override
    public User updateUserService(User user, int userId) {
        User updatedUser = userRepository.getUserById(userId);
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            boolean hasEmail = userRepository.getUserStorage().values().stream()
                    .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()) && user1.getId() != userId);
            if (hasEmail) {
                throw new DubleException("Такой емейл уже существует");
            }
            updatedUser.setEmail(user.getEmail());
        }

        updatedUser.setId(userId);
        return userRepository.updateUserRepo(updatedUser, userId);
    }

    @Override
    public User deleteUserService(int userId) {
        return userRepository.deleteUserRepo(userId);
    }

    @Override
    public User getUserSerivece(int userId) {
        return userRepository.getUserById(userId);
    }
}
