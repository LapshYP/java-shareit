package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @SneakyThrows
    @Override
    public User createUserSerivce(User user) {

       userRepository.createUserRepo(user);
            return user;

    }

    @Override
    public List<User> getAll() {
        return userRepository.getAllUsers();
    }

    @Override
    public User updateUserService(User user, int userId) {
        return userRepository.updateUserRepo(user,userId);
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
