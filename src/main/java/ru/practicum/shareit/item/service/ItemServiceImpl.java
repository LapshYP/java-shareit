package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepositoryImpl userRepository;
    private final int id = 1;

    @SneakyThrows
    @Override
    public Item createService(Item item, int userId) {

        if (item.getAvailable() == null) {
            log.error("Вещь с именем = {} и описанием {} не доступна", item.getName(), item.getDescription());
            throw new BadRequestException(HttpStatus.BAD_REQUEST, item.getName() + " не доступна");
        }
        if (!userRepository.getUserStorage().keySet().contains(userId)) {
            log.error("Пользователя с id= {} нет в базе данных", userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных");
        }
        if (itemRepository.getItemStorage().values()
                .stream()
                .filter(item1 -> (item1.getName().equals(item.getName())
                        && item1.getDescription().equals(item.getDescription()))).count() > 0) {
            log.error("Вещь с именем = {} и описанием {} уже создана", item.getName(), item.getDescription());
            throw new DubleException(item.getName() + " уже создана");
        }
        item.setId(id++);
        item.setOwnerId(userId);
        log.debug("Вещь с именем = {} и описанием {} создана", item.getName(), item.getDescription());
        return itemRepository.createItemRepo(item, userId);
    }

    @Override
    public Item updateService(Item item, int itemId, int userId) {
        boolean isRightUser = itemRepository.getItemStorage().get(itemId).getOwnerId() == userId ? true : false;
        if (!isRightUser) {
            log.error("Вещь с именем = {} и описанием {} не может быть обновлена этим пользователем {}", item.getName(), userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не может быть обновлена этим пользователем");
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
        log.debug("Вещь с именем = {} и описанием {} обновлена", item.getName(), item.getDescription());
        return itemRepository.updateItemRepo(updateItem, itemId, userId);
    }

    @Override
    public Item getByIdService(int itemId, int userId) {
        log.debug("Вещь с id = {} созданная {} предоставлена", itemId, userId);
        return itemRepository.getItemById(itemId, userId);
    }

    @Override
    public List<Item> getByUserIdService(int userId) {
        log.debug("Список всех вещей предоставлен");
        return itemRepository.getItemByUserId(userId);
    }

    @Override
    public List<Item> searchByParamService(String text) {
        String textToLowerCase = text.toLowerCase();
        if (text == null || text.isEmpty()) {
            log.debug("Вещь по запросу {} не найдена", text);
            return new ArrayList<>();
        } else {
            log.debug("Вещь по запросу {} найдена", text);
            return itemRepository.itemSearchByParamService(textToLowerCase);
        }
    }
}
