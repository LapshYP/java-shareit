package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepoJpa itemRepoJpa;
    private final UserRepoJpa userRepoJpa;
    private final ItemMapper itemMapper
            = Mappers.getMapper(ItemMapper.class);


    @SneakyThrows
    @Override
    public ItemDTO createService(ItemDTO itemDTO, int userId) {
        Item item = itemMapper.itemDTOToItem(itemDTO);

        if (item.getAvailable() == null) {
            log.error("Вещь с именем = {} и описанием {} не доступна", item.getName(), item.getDescription());
            throw new BadRequestException(HttpStatus.BAD_REQUEST, item.getName() + " не доступна");
        }
        if (!(userRepoJpa.findAll().stream().filter(user -> user.getId()==userId).count() >0)) {
            log.error("Пользователя с id= {} нет в базе данных", userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных");
        }
        if (itemRepoJpa.findAll()
                .stream()
                .filter(item1 -> (item1.getName().equals(item.getName())
                        && item1.getDescription().equals(item.getDescription()))).count() > 0) {
            log.error("Вещь с именем = {} и описанием {} уже создана", item.getName(), item.getDescription());
            throw new DubleException(item.getName() + " уже создана");
        }

        item.setOwner(userRepoJpa.getReferenceById(userId));
        log.debug("Вещь с именем = {} и описанием {} создана", item.getName(), item.getDescription());
        Item createdItem = itemRepoJpa.save(item);
        ItemDTO createdItemDTO = itemMapper.itemToItemDTO(createdItem);
        return createdItemDTO;
    }

    @Override
    public ItemDTO updateService(ItemDTO itemDTO, int itemId, int userId) {
        Item item = itemMapper.itemDTOToItem(itemDTO);

        boolean isRightItem = itemRepoJpa.getById(itemId) != null ? true : false;
        if (!isRightItem) {
            log.error("Вещь с именем = {} и индификатором {} не существует", item.getName(), itemId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь c id = '" + itemId + " не существует");
        }
        boolean isRightUser = itemRepoJpa.getById(itemId).getOwner().getId() == userId ? true : false;
        if (!isRightUser) {
            log.error("Вещь с именем = {} и описанием {} не может быть обновлена", item.getName(), userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не может быть обновлена этим пользователем");
        }

        Item updateItem = itemRepoJpa.getById(itemId);
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
        updateItem.setOwner(userRepoJpa.getReferenceById(userId));
        Item updatedItem = itemRepoJpa.save(updateItem);
        ItemDTO updatedItemDTO = itemMapper.itemToItemDTO(updatedItem);
        return updatedItemDTO;
    }

    @Override
    public ItemDTO getByIdService(int itemId, int userId) {
        if (!(userRepoJpa.findAll().stream().filter(user -> user.getId()==userId).count() >0)) {
            log.error("Пользователя с id= {} нет в базе данных", userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных");
        }
        if (!(itemRepoJpa.findAll().stream().filter(item -> item.getId()==itemId).count() >0)) {
            log.error("Вещи с id= {} нет в базе данных", itemId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь с id  = '" + itemId + " нет в базе данных");
        }

        ItemDTO itemDTO = itemMapper.itemToItemDTO(itemRepoJpa.getById(itemId));
        log.debug("Вещь с id = {} созданная {} просмотрена", itemId, userId);
        return itemDTO;
    }

    @Override
    public List<ItemDTO> getByUserIdService(int userId) {
        if (!(userRepoJpa.findAll().stream().filter(user -> user.getId()==userId).count() >0)) {
            log.error("Пользователя с id= {} нет в базе данных", userId);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных");
        }
        log.debug("Список всех вещей просмотрен");

        return itemRepoJpa.findAll()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .collect(Collectors.toList())

                .stream()
                .map(itemMapper::itemToItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> searchByParamService(String text) {
        if (text == null || text.isEmpty()) {
            log.debug("Запрос не задан");
            return new ArrayList<>();
        }
        String textToLowerCase = text.toLowerCase();
        if (text == null || text.isEmpty()) {
            log.debug("Вещь по запросу {} не найдена", text);
            return new ArrayList<>();
        } else {
            log.debug("Вещь по запросу {} найдена", text);
            return itemRepoJpa.findAll()
                    .stream()
                    .filter(item -> (item.getName().toLowerCase().contains(textToLowerCase)
                            || item.getDescription().toLowerCase().contains(textToLowerCase)
                            && item.getAvailable()))
                    .collect(Collectors.toList())

                    .stream()
                    .map(itemMapper::itemToItemDTO)
                    .collect(Collectors.toList());
        }
    }
}
