package ru.practicum.shareit.booking.repossitory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepoJpa extends PagingAndSortingRepository<Booking, Integer> {

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "ORDER BY b.start DESC ")
    List<Booking> getAllForOwner(int userOwner,Pageable paging);

    List<Booking> findAllByBookerAndStartIsAfterOrderByStartDesc(User userBooker, LocalDateTime now, Pageable paging);

    List<Booking> findAllByBookerOrderByStartDesc(User userBooker, Pageable paging);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerAndStartIsAfterOrderByStartDesc(int id, LocalDateTime timeNow, Pageable paging);

    @Query("select b from Booking b " +
            "where b.booker = ?1 and b.start < ?2 and b.end > ?3 " +
            "order by b.start DESC ")
    List<Booking> findAllBookingsForBookerWithStartAndEnd(User userBooker, LocalDateTime now, LocalDateTime now1, Pageable paging);

    List<Booking> findAllByBookerAndStatusEqualsOrderByStartDesc(User userBooker, Status status, Pageable paging);

    @Query("SELECT b " +
            "FROM Booking AS b " +
            "JOIN b.item AS i " +
            "WHERE i.owner.id = ?1 " +
            "AND b.status = ?2 " +
            "ORDER BY b.start DESC ")
    List<Booking> findAllByOwnerAndStatusEqualsOrderByStartDesc(int userOwnerId, Status status, Pageable paging);

    @Query("SELECT b from Booking b " +
            "WHERE b.item.owner = ?1 " +
            "and b.start < ?2 and b.end > ?3 " +
            "order by b.start DESC")
    List<Booking> findAllBookingsForOwnerWithStartAndEnd(User owner, LocalDateTime now, LocalDateTime now1, Pageable paging);

    @Query("SELECT b from Booking b " +
            "where b.item.owner = ?1 " +
            "and b.end < ?2 " +
            "order by b.start DESC")
    List<Booking> findAllByOwnerAndEndIsBeforeOrderByStartDesc(User owner, LocalDateTime now, Pageable paging);

    List<Booking> findAllByBookerAndEndIsBeforeOrderByStartDesc(User booker, LocalDateTime now, Pageable paging);

}
