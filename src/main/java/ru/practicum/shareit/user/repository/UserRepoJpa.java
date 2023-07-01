package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;


public interface UserRepoJpa extends JpaRepository<User, Integer> {

   // @Query(value = "SELECT * FROM USERS WHERE ID = ?1 AND EMAIL = ?2", nativeQuery = true)
    List<User> findByIdAndEmail(int id, String email);
//    @Query(value = "SELECT * FROM USERS WHERE EMAIL = ?1", nativeQuery = true)
//    List<User> findByEmail(String email);
@Query(value = "SELECT * FROM USERS WHERE EMAIL = ?1", nativeQuery = true)
    List<User> findByEmailEquals(String email);

}
