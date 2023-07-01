package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Repository
public interface ItemRepoJpa extends JpaRepository<Item, Integer> {
    List<Item> findAllByOwnerOrderById(User owner);

    @Query("select it " +
            "from Item as it " +
            "where (lower(it.name) like concat('%', ?1, '%') " +
            "or lower(it.description) like concat('%', ?1, '%')) " +
            "and it.available = true ")
    List<Item> searchByParam(String text);

}
