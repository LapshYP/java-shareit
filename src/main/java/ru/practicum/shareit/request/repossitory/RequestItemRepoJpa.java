package ru.practicum.shareit.request.repossitory;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;

import java.util.Collection;
import java.util.List;

@Repository
public interface RequestItemRepoJpa extends PagingAndSortingRepository<Request,Integer> {
//public interface RequestItemRepoJpa extends JpaRepository<Request,Integer> {
    List<Request> findAllByRequestor_Id(int requesterId, Pageable pageable);

    List<Request> findAllByRequestor_Id(int userId);
    @Query("SELECT i FROM Request i WHERE i.requestor.id <> ?1 ORDER BY i.createdtime DESC")
    List<Request> findByOwnerId(int userId, Pageable pageable);

    // List<Request>getAllByRequestor_IdOrderByCreatedtimeDesc(int requesterId);

    //getAllByRequester_IdOrderByCreatedDesc(int requesterId);
}
