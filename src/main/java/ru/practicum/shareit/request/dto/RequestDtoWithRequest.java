package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDtoWithRequest {
    int id;
    String description;
    UserDTO requestor;
    @JsonProperty("created")
    LocalDateTime createdtime;

    List<ItemDTO> items = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestDtoWithRequest)) return false;
        RequestDtoWithRequest that = (RequestDtoWithRequest) o;
        return getId() == that.getId() && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }
}
