package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepoJpa extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerOrderById(User owner);


//    Optional<Item> findItemByNameAndDescription(String name, String description);

}
