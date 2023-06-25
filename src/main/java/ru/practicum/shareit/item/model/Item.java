package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    int id;
    @NotBlank
    @Column
    String name;
    @NotBlank
    @NotNull
    @Column
    String description;
    @NotNull
    @Column
    Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column
    int request;

   // int ownerId;


//    @ElementCollection
//    @CollectionTable(name="tags", joinColumns=@JoinColumn(name="item_id"))
//    @Column(name="name")
//    private Set<String> tags = new HashSet<>();

    @ElementCollection
    @OneToMany(mappedBy = "item")
    private Set<Booking> bookings;

}