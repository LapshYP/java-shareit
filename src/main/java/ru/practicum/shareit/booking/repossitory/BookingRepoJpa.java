package ru.practicum.shareit.booking.repossitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepoJpa extends JpaRepository<Booking, Integer> {

     @Query("SELECT b" +
             " FROM Booking AS b" +
             " WHERE b.booker = ?1" +
             " ORDER BY b.start DESC")
    List<Booking> getAllForUser(User userBooker);
    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "ORDER BY b.start DESC ")
     List<Booking> getAllForOwner(int userOwner);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User userBooker, LocalDateTime now);
    List<Booking> findAllByBookerOrderByStartDesc(User userBooker);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerAndStartIsAfterOrderByStartDesc(int id, LocalDateTime timeNow);
}
