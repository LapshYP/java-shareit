package ru.practicum.shareit.user.service;


import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDTO;

import java.util.List;
@Transactional(readOnly = true)
public interface UserService {
    @Transactional
    UserDTO createUserSerivce(UserDTO userDTO);

    List<UserDTO> getAll();

    UserDTO updateUserService(UserDTO userDTO, int userId);

    UserDTO deleteUserService(int userId);

    UserDTO getUserSerivece(int userId);

}
