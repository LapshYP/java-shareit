package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comment.Comment;
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
@Builder
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