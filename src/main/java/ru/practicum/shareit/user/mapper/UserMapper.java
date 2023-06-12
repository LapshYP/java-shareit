package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);
}