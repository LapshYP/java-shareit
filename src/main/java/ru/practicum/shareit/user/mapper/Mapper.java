package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDTO;

@Component
public class Mapper {
    public UserDTO toDto(User user) {
        int id = user.getId();
        String name = user.getName();
         String email = user.getEmail();
        return new UserDTO(id,name, email);
    }

}