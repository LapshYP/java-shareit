package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "items", schema = "public")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    int id;
    @Column
    @NotBlank
    String name;
    @Column
    @NotBlank
    String description;
    @Column
    @NotNull
    Boolean available;

    @Column
    int request;

    @OneToMany(mappedBy = "item")
    private List<Booking> bookings;
    @OneToMany(mappedBy = "item")
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
     User owner;

   // @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "requestitem_id")
//    //@JsonBackReference
//   //  @JsonIgnore
//    Request requestItem;
}