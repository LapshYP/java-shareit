package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDTO;

import javax.persistence.Table;
import java.time.LocalDateTime;


@Table(name = "requests", schema = "public")
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
