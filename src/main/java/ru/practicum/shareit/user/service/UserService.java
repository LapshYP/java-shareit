package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User createUserSerivce(User user);

    List<User> getAll();

    User updateUserService(User user, int userId);

    User deleteUserService(int userId);

    User getUserSerivece(int userId);

}
