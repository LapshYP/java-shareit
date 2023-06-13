package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private UserMapper userMapper
            = Mappers.getMapper(UserMapper.class);

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        User savedUser = userService.createUserSerivce(user);
        UserDTO savedUserDTO = userMapper.userToUserDTO(savedUser);
        return savedUserDTO;
    }

    @PatchMapping("/{userId}")
    public UserDTO updateUser(@RequestBody UserDTO userDTO, @PathVariable int userId) {
        User user = userMapper.userDTOToUser(userDTO);
        User updatedUser = userService.updateUserService(user, userId);
        UserDTO updatedUserDTO = userMapper.userToUserDTO(updatedUser);
        return updatedUserDTO;
    }

    @DeleteMapping("/{userId}")
    public UserDTO deleteUser(@PathVariable int userId) {
        User user = userService.deleteUserService(userId);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable int userId) {
        User user = userService.getUserSerivece(userId);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        return userDTO;
    }

    @GetMapping
    @ResponseBody
    public List<UserDTO> getUsers() {
        return userService.getAll()
                .stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }
}
