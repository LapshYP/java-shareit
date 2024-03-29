package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.Validation;

import javax.validation.Valid;
import javax.validation.ValidationException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserControllerGatway {
    private final UserClient userClient;

    @PostMapping
    @Validated(Validation.Post.class)
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDTO userDto) {
        return userClient.addUser(userDto);
    }

    @PatchMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Validated(Validation.Patch.class)
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO,
                                             @PathVariable("id") int id) {
        if (userDTO.getName() != null && userDTO.getName().isBlank()) {
            throw new ValidationException("поле имени не может быть пустым");
        }
        if (userDTO.getEmail() != null && userDTO.getEmail().isBlank()) {
            throw new ValidationException("поле email не может быть пустым");
        }
        return userClient.updateUserService(userDTO, id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {

        return userClient.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable int id) {
        return userClient.getUserSerivece(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable int id) {
        return userClient.deleteUserService(id);
    }
}