package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepoJpa userRepoJpa;

    private final ModelMapper mapper = new ModelMapper();

    @SneakyThrows
    @Override

    public UserDTO createUserSerivce(UserDTO userDTO) {
        User user = mapper.map(userDTO, User.class);
        User savedUser = userRepoJpa.save(user);
        log.debug("Пользователь с email = {} и именем {} добавлен", user.getEmail(), user.getName());
        UserDTO savedUserDTO = mapper.map(savedUser, UserDTO.class);
        return savedUserDTO;
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepoJpa.findAll().stream()
                .map(user -> {
                    return mapper.map(user, UserDTO.class);
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public UserDTO updateUserService(UserDTO userDTO, int userId) {
        User user = mapper.map(userDTO, User.class);
        User updatedUser = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = '" + userId + "' не найден"));
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            int userCount = userRepoJpa.findByIdAndEmail(updatedUser.getId(), updatedUser.getEmail()).size();
            if (userCount != 1) {
                log.error("Пользователь с email = {} уже существует", user.getEmail());
                throw new DubleException("Такой емейл уже существует");
            }
            updatedUser.setEmail(user.getEmail());
        }

        updatedUser.setId(userId);
        userRepoJpa.save(updatedUser);
        log.debug("Пользователь с email = {} и именем {} обновлен", user.getEmail(), user.getName());
        UserDTO updatedUserDTO = mapper.map(updatedUser, UserDTO.class);
        return updatedUserDTO;
    }

    @Override
    public UserDTO deleteUserService(int userId) {
        User user = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = '" + userId + "' не найден"));
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        userRepoJpa.deleteById(userId);
        log.debug("Пользователь с userId = {} удален", userId);
        return userDTO;
    }

    @Override
    public UserDTO getUserSerivece(int userId) {
        User user = userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = '" + userId + "' не найден"));
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        log.debug("Пользователь с userId = {} просмотрен", userId);
        return userDTO;
    }
}
