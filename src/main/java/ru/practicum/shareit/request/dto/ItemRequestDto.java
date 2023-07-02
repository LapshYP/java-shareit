package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Table(name = "requests",schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    int id;
    String description;
  //  @JsonProperty("requestorName")
//    int requestor_id;
   @JsonProperty("created")
    LocalDateTime createdtime;
   ItemDTO itemDTO;
}
