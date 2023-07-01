package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO createUserSerivce(UserDTO userDTO);

    List<UserDTO> getAll();

    UserDTO updateUserService(UserDTO userDTO, int userId);

    UserDTO deleteUserService(int userId);

    UserDTO getUserSerivece(int userId);

}
