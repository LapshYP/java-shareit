package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.Mapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Mapper mapper;

    @PostMapping
    public User createUser (@Valid @RequestBody User user){

        return userService.createUserSerivce(user);
    }

    @PatchMapping("/{userId}")
    public User updateUser (@RequestBody User user, @PathVariable int userId) {

        return userService.updateUserService(user,userId);
    }

    @DeleteMapping("/{userId}")
    public User deleteUser (@PathVariable int userId){
        return userService.deleteUserService(userId);
    }

    @GetMapping("/{userId}")
    public User getUser (@PathVariable int userId){
        return userService.getUserSerivece(userId);
    }

    @GetMapping
    @ResponseBody
    public List<UserDTO> getUsers() {
        return userService.getAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

}
