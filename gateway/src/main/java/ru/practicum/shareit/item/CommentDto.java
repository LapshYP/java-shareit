package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CommentDto {
    private Long id;
    @JsonProperty("text")
    @NotBlank(message = "Комментарий не может быть пустым")
    private String content;
    private String authorName;
    private LocalDateTime created;

}