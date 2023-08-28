package ru.practicum.shareit.user;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.Validation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDTO {
    int id;
    @NotEmpty(groups = Validation.Post.class, message = "must not be blank")
    private String name;
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(groups = Validation.Post.class, message = "must not be blank")
    private String email;

}
