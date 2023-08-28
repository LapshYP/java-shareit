package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface CommentRepoJpa extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemOrderById(Item item);
}