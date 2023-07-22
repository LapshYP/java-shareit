package ru.practicum.shareit.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RequestDTO {
    int id;
    @NotBlank(message = "must not be blank")
    String description;
    @JsonProperty("created")
    LocalDateTime createdtime;
}
