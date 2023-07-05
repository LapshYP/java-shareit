package ru.practicum.shareit.request.repossitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

@Repository
public interface RequestItemRepoJpa extends JpaRepository<ItemRequest,Integer> {

}
