package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class BookingDto {
    private int id;
    @NotNull
    private int itemId;
  // @JsonAlias({"start"})
   @JsonProperty( "start" )
    private LocalDateTime start;
   // @JsonAlias({"end"})
    @JsonProperty( "end" )
    private LocalDateTime end;
    private Item item;
    private User booker;
    private Status status;


}
