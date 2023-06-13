package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepositoryImpl userRepository;
    private static int id = 1;

    @SneakyThrows
    @Override
    public Item createService(Item item, int userId) {

        if (item.getAvailable() == null) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, item.getName() +" не доступна");
        }
        if (!userRepository.getUserStorage().keySet().contains(userId)) {

            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id = '"+userId+" нет в базе данных");
        }
        if (itemRepository.getItemStorage().values()
                .stream()
                .filter(item1 -> (item1.getName().equals(item.getName())
                        && item1.getDescription().equals(item.getDescription()))).count() > 0) {
            throw new DubleException(item.getName() + " уже создана");
        }


        item.setId(id++);
        item.setOwnerId(userId);
        return itemRepository.createItemRepo(item, userId);
    }

    @Override
    public Item updateService(Item item, int itemId, int userId) {
        boolean isRightUser = itemRepository.getItemStorage().get(itemId).getOwnerId() == userId ? true : false;
        if (!isRightUser) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "item can't update with other user");
        }
        Item updateItem = itemRepository.getItemById(itemId);
        if (item.getName() != null) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }

        return itemRepository.updateItemRepo(updateItem, itemId, userId);
    }

    @Override
    public Item getByIdService(int itemDtoId, int userId) {
        return itemRepository.getItemById(itemDtoId, userId);
    }

    @Override
    public List<Item> getByUserIdService(int userId) {
        return itemRepository.getItemByUserId(userId);
    }

    @Override
    public List<Item> searchByParamService(String text) {

        String textToLowerCase = text.toLowerCase();
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        } else {
            return itemRepository.itemSearchByParamService(textToLowerCase);
        }

    }

}
