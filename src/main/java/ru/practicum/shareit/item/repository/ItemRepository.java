package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ItemRepository {
    Item createItemRepo(Item item, int userId);
    Item updateItemRepo(Item item, int itemId, int userId);
    Item getItemById(int itemDtoId, int userId);
    List<Item> getItemByUserId(int userId);
    List<Item> itemSearchByParamService(String text);
    Item getItemById(int itemId);
    HashMap<Integer, Item> getItemStorage();

}
