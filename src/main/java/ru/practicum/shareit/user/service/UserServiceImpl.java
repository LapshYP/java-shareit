package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper
            = Mappers.getMapper(UserMapper.class);
    private int id = 1;

    @SneakyThrows
    @Override
    public UserDTO createUserSerivce(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);

        if (userRepository.getUserStorage().values()
                .stream()
                .filter(user1 -> user1.getEmail().equals(user.getEmail())).count() > 0) {
            log.error("Пользователь с email = {} и именем {} уже существует", user.getEmail(), user.getName());
            throw new DubleException("Уже существует");
        }

        user.setId(id++);
        userRepository.createUserRepo(user);
        log.debug("Пользователь с email = {} и именем {} добавлен", user.getEmail(), user.getName());

        UserDTO savedUserDTO = userMapper.userToUserDTO(user);
        return savedUserDTO;
    }

    @Override
    public List<UserDTO> getAll() {

        return userRepository.getAllUsers().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public UserDTO updateUserService(UserDTO userDTO, int userId) {
        User user = userMapper.userDTOToUser(userDTO);


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
        userRepository.updateUserRepo(updatedUser, userId);
        log.debug("Пользователь с email = {} и именем {} обновлен", user.getEmail(), user.getName());

        UserDTO updatedUserDTO = userMapper.userToUserDTO(updatedUser);
        return updatedUserDTO;
    }

    @Override
    public UserDTO deleteUserService(int userId) {
        User user = userRepository.deleteUserRepo(userId);
        log.debug("Пользователь с userId = {} удален", userId);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;
    }

    @Override
    public UserDTO getUserSerivece(int userId) {

        if (userRepository.getUserStorage().values()
                .stream()
                .filter(user1 -> user1.getId() == userId).count() == 0) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = '" + userId + "' не найден");
        }
        User user = userRepository.getUserById(userId);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        log.debug("Пользователь с userId = {} просмотрен", userId);
        return userDTO;
    }
}
