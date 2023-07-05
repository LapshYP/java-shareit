package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUserSerivce(userDTO);
    }

    @PatchMapping("/{userId}")
    public UserDTO updateUser(@RequestBody UserDTO userDTO, @PathVariable int userId) {
        return userService.updateUserService(userDTO, userId);
    }

    @DeleteMapping("/{userId}")
    public UserDTO deleteUser(@PathVariable int userId) {
        return userService.deleteUserService(userId);
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable int userId) {
        return userService.getUserSerivece(userId);
    }

    @GetMapping
    @ResponseBody
    public List<UserDTO> getUsers() {
        return userService.getAll();
    }
}
