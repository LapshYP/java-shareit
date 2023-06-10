package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    // HashMap< USER_ID, ItemDto>
    private final HashMap<Integer, HashSet<ItemDto>> itemStorage = new HashMap<>();
    // HashMap< ITEM_ID, HashMap<USER_ID, ItemDto>>
    private final HashMap<Integer, HashMap<Integer, ItemDto>> itemStorageWithUser = new HashMap<>();
    private final UserRepositoryImpl userRepository;
    private static int id = 1;

    @Override
    public ItemDto createItemRepo(ItemDto itemDto, int userId) {
        if (itemDto.getAvailable() == null) {
            throw new NotFoundException(HttpStatus.BAD_REQUEST, "Вещь не доступна");
        } else if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            throw new NotFoundException(HttpStatus.BAD_REQUEST, "Нет описания");
        } else if (itemDto.getName().isBlank()) {
            throw new NotFoundException(HttpStatus.BAD_REQUEST, "Нет имени");
        } else if (!userRepository.getUserList().keySet().contains(userId)) {

            throw new NotFoundException(HttpStatus.NOT_FOUND, "Юзера нет в базе данных");
        } else {
            itemDto.setId(id++);
            itemStorage.put(userId, new HashSet<>(Arrays.asList(itemDto)));

            itemStorageWithUser.put(itemDto.getId(), new HashMap<>() {{
                put(userId, itemDto);
            }});
        }

        return itemDto;
    }

    @Override
    public ItemDto updateItemRepo(ItemDto itemDto, int itemId, int userId) {
//        boolean hasEmail = userList.values().stream()
//                .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()) && user1.getId() != userId);
//        if (hasEmail) {
//            throw new DoubleException("Такой емейл уже существует");
//        }
//
        boolean isRightUser = itemStorageWithUser.get(itemId).keySet().contains(userId);
        if (!isRightUser) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "item can't update with other user");
        }
        ItemDto updateItem = getItemById(itemId);
        if (itemDto.getName() != null) {
            updateItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            updateItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            updateItem.setAvailable(itemDto.getAvailable());
        }
        itemStorage.put(userId, new HashSet<>(Arrays.asList(updateItem)));

        itemStorageWithUser.put(itemId, new HashMap<>() {{
            put(userId, itemDto);
        }});

        return updateItem;
    }

    @Override
    public ItemDto getItemById(int itemDtoId, int userId) {

        return itemStorageWithUser.get(itemDtoId).get(itemDtoId);
    }

    @Override
    public Collection<ItemDto> getItemByUserId(int userId) {

        return itemStorage.get(userId);
    }

    @Override
    public List<ItemDto> itemSearchByParamService(String text) {
        return null;
    }


    private ItemDto getItemById(int itemId) {

        return itemStorage.entrySet().stream()
                .flatMap(map -> map.getValue().stream())
                .filter(itemDto -> itemDto.getId() == itemId)
                .findFirst().orElse(null);
    }
}
