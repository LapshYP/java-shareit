package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private int id = 1;

    @SneakyThrows
    @Override
    public User createUserSerivce(User user) {
        if (userRepository.getUserStorage().values()
                .stream()
                .filter(user1 -> user1.getEmail().equals(user.getEmail())).count() > 0) {
            log.error("Пользователь с email = {} и именем {} уже существует", user.getEmail(), user.getName());
            throw new DubleException("Уже существует");
        }
        user.setId(id++);
        userRepository.createUserRepo(user);
        log.debug("Пользователь с email = {} и именем {} добавлен", user.getEmail(), user.getName());
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
                log.error("Пользователь с email = {} уже существует", user.getEmail());
                throw new DubleException("Такой емейл уже существует");
            }
            updatedUser.setEmail(user.getEmail());
        }
        updatedUser.setId(userId);
        log.debug("Пользователь с email = {} и именем {} обновлен", user.getEmail(), user.getName());
        return userRepository.updateUserRepo(updatedUser, userId);
    }

    @Override
    public User deleteUserService(int userId) {
        log.debug("Пользователь с userId = {} удален", userId);
        return userRepository.deleteUserRepo(userId);
    }

    @Override
    public User getUserSerivece(int userId) {
        log.debug("Пользователь с userId = {} просмотрен", userId);
        if (userRepository.getUserStorage().values()
                .stream()
                .filter(user1 -> user1.getEmail().equals(userId)).count() == 0) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = '" + userId + "' не найден");
        }
        return userRepository.getUserById(userId);
    }
}
