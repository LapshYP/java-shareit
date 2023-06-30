package ru.practicum.shareit.booking.repossitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepoJpa extends JpaRepository<Booking, Integer> {


    //     @Query("SELECT b" +
//             " FROM Booking AS b" +
//             " WHERE b.booker = ?1" +
//             " ORDER BY b.start DESC")
//    List<Booking> getAllForUser(User userBooker);
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

    @Query("select b from Booking b " +
            "where b.booker = ?1 and b.start < ?2 and b.end > ?3 " +
            "order by b.start asc ")
    List<Booking> findAllBookingsForBookerWithStartAndEnd(User userBooker, LocalDateTime now, LocalDateTime now1);

    //
    List<Booking> findAllByBookerAndStartIsBeforeOrderByStartDesc(User userBooker, LocalDateTime now);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User userBooker, Status status);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerAndStatusEqualsOrderByStartDesc(int userOwnerId, Status status);

    @Query("SELECT b from Booking b " +
            "JOIN b.item AS i " +
            "WHERE i.owner = ?1 " +
            "and b.start < ?2 and b.end > ?3 " +
            "order by b.start DESC")
    List<Booking> findAllBookingsForOwnerWithStartAndEnd(User owner, LocalDateTime now, LocalDateTime now1);

    @Query("SELECT b from Booking b " +
            "where b.item.owner = ?1 " +
            "and b.end < ?2 " +
            "order by b.start DESC")
    List<Booking> findAllByOwnerAndEndIsBeforeOrderByStartDesc(User owner, LocalDateTime now);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime now);


    // List<Booking> findAllByBookerAndStateEqualsOrderByStartTimeDesc(User userBooker, State waiting);

}
