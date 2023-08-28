package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Data
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserDTO {
    int id;
    String name;
    String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(getName(), userDTO.getName()) && Objects.equals(getEmail(), userDTO.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail());
    }
}
