package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
