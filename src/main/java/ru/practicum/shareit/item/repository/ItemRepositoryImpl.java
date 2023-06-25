package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Integer, Item> itemStorageWithUser = new HashMap<>();

    @Override
    public Item createItemRepo(Item item, int userId) {
        itemStorageWithUser.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItemRepo(Item updateItem, int itemId, int userId) {
        itemStorageWithUser.put(itemId, updateItem);
        return updateItem;
    }

    @Override
    public Item getItemById(int itemDtoId, int userId) {
        return itemStorageWithUser.get(itemDtoId);
    }

    @Override
    public List<Item> getItemByUserId(int userId) {
        return itemStorageWithUser
                .values()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> itemSearchByParamService(String textToLowerCase) {
        return itemStorageWithUser
                .values()
                .stream()
                .filter(item -> (item.getName().toLowerCase().contains(textToLowerCase)
                        || item.getDescription().toLowerCase().contains(textToLowerCase)
                        && item.getAvailable()))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(int itemId) {
        return itemStorageWithUser.get(itemId);
    }

    @Override
    public HashMap<Integer, Item> getItemStorage() {
        return itemStorageWithUser;
    }
}
