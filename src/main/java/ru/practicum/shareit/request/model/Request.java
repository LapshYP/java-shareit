package ru.practicum.shareit.request.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests", schema = "public")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    int id;
    @NotNull
    @Column
    String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id")
    @JsonProperty("requestorName")
    User requestor;
    @Column
    @JsonProperty("created_time")
    LocalDateTime createdtime;

    @OneToMany
            (mappedBy = "request", orphanRemoval = true,
                    cascade = CascadeType.ALL)
    @Column(nullable = true)
    //@JoinColumn(name = "request_id")
    @JsonIgnore
    // @JsonManagedReference
    List<Item> items;
}
