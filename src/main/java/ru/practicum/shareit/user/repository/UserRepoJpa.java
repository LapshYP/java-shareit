package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface UserRepoJpa extends JpaRepository<User, Integer> {

    List<User> findByIdAndEmail(int id, String email);


}
