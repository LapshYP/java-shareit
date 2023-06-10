package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {

    private final HashMap<Integer, ItemDto> itemStorageWithUser = new HashMap<>();
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
            itemDto.setOwnerId(userId);

            itemStorageWithUser.put(itemDto.getId(), itemDto);
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
        boolean isRightUser = itemStorageWithUser.get(itemId).getOwnerId() == userId ? true : false;
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


        itemStorageWithUser.put(itemId, updateItem);

        return updateItem;
    }

    @Override
    public ItemDto getItemById(int itemDtoId, int userId) {

        return itemStorageWithUser.get(itemDtoId);
    }

    @Override
    public List<ItemDto> getItemByUserId(int userId) {

        return itemStorageWithUser.values().stream().filter(item -> item.getOwnerId()==userId).collect(Collectors.toList());
    }

    //|| item.getDescription().toLowerCase().contains(textToLowerCase)
    @Override
    public List<ItemDto> itemSearchByParamService(String text) {
        String textToLowerCase = text.toLowerCase();
        return itemStorageWithUser.values().stream().filter(item -> (item.getName().toLowerCase().contains(textToLowerCase)  && item.getDescription().toLowerCase().contains(textToLowerCase))).collect(Collectors.toList());
    }


    private ItemDto getItemById(int itemId) {

        return itemStorageWithUser.get(itemId);
    }
}
