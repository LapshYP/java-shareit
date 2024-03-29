package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingLastNextItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepoJpa;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemLastNextDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepoJpa;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepoJpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepoJpa itemRepoJpa;
    private final UserRepoJpa userRepoJpa;
    private final CommentRepoJpa commentRepoJpa;

    private final ModelMapper mapper = new ModelMapper();


    @SneakyThrows
    @Override
    public ItemDTO createService(ItemDTO itemDTO, int userId) {

        Item item = mapper.map(itemDTO, Item.class);

        item.setOwner(userRepoJpa.getReferenceById(userId));
        item.setRequest(itemDTO.getRequestId());


        userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = '" + userId + "' не найден"));

        Item createdItem = itemRepoJpa.save(item);
        log.debug("Вещь с именем = {} и описанием {} создана", item.getName(), item.getDescription());

        ItemDTO createdItemDTO = mapper.map(createdItem, ItemDTO.class);
        createdItemDTO.setRequestId(itemDTO.getRequestId());
        return createdItemDTO;
    }

    @Override
    public ItemDTO updateService(ItemDTO itemDTO, int itemId, int userId) {
        Item item = mapper.map(itemDTO, Item.class);

        Item updateItem = itemRepoJpa.findById(itemId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь c id = '" + item.getId() + "' не существует"));

        userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не может быть обновлена этим пользователем id = '" + userId + "' "));

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
        ItemDTO updatedItemDTO = mapper.map(updatedItem, ItemDTO.class);
        return updatedItemDTO;
    }

    @Override
    public ItemLastNextDTO getByOwnerIdService(int itemId, int userId) {

        Item item = itemRepoJpa.findById(itemId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь с id  = '" + itemId + " нет в базе данных"));

        User owner = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных"));

        ItemLastNextDTO itemLastNextDTO = getItemLastNextDTO(userId, item);


        List<Comment> comments = item.getComments();
        List<CommentDto> commentDtoForResponse = comments
                .stream()
                .map(comment -> {
                    return mapper.map(comment, CommentDto.class);
                })
                .collect(Collectors.toList());
        itemLastNextDTO.setComments(commentDtoForResponse);
        log.debug("Вещь с id = {} созданная {} просмотрена", itemId, userId);
        return itemLastNextDTO;
    }

    public ItemLastNextDTO getItemLastNextDTO(int userId, Item item) {
        List<Booking> allBookings = item.getBookings();

        Booking lastBooking = null;
        Booking nextBooking = null;
        LocalDateTime now = LocalDateTime.now();
        int ownerId = item.getOwner().getId();
        if (ownerId == userId && allBookings != null) {
            int sizeLast = allBookings
                    .stream()
                    .filter(booking -> now.isAfter(booking.getStart()))
                    .collect(Collectors.toList()).size();
            if (sizeLast != 0) {
                lastBooking = allBookings
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart).reversed())
                        .filter(booking -> now.isAfter(booking.getStart()))
                        .collect(Collectors.toList()).get(0);
            }
            int sizeNext = allBookings
                    .stream()
                    .filter(booking -> now.isBefore(booking.getStart()))
                    .collect(Collectors.toList()).size();
            if (sizeNext != 0) {
                nextBooking = allBookings
                        .stream()
                        .sorted(Comparator.comparing(Booking::getStart))
                        .filter(booking -> now.isBefore(booking.getStart()))
                        .collect(Collectors.toList()).get(0);
            }
        }
        ItemLastNextDTO itemLastNextDTO = mapper.map(item, ItemLastNextDTO.class);
        if (lastBooking != null && lastBooking.getStatus() != Status.REJECTED) {
            itemLastNextDTO.setLastBooking(mapper.map(lastBooking, BookingLastNextItemDto.class));
        } else itemLastNextDTO.setLastBooking(null);

        if (nextBooking != null && nextBooking.getStatus() != Status.REJECTED) {
            itemLastNextDTO.setNextBooking(mapper.map(nextBooking, BookingLastNextItemDto.class));
        } else itemLastNextDTO.setNextBooking(null);
        return itemLastNextDTO;
    }

    @Override
    public List<ItemLastNextDTO> getByBookerIdService(int userId, int from, int size) {
        log.debug("Список всех вещей просмотрен");
        User booker = userRepoJpa.findById(userId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных"));

        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "не правильный параметр пагинации");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> items = itemRepoJpa.findAllByOwnerOrderById(booker, pageable);
        List<ItemLastNextDTO> itemLastNextDTOList = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Item item : items) {
            ItemLastNextDTO itemLastNextDTO = mapper.map(item, ItemLastNextDTO.class);
            List<Booking> allBookings = item.getBookings();

            Booking lastBooking = null;

            int sizeLast = allBookings.stream()
                    .filter(booking -> now.isAfter(booking.getStart()))
                    .collect(Collectors.toList()).size();
            if (sizeLast != 0) {
                lastBooking = allBookings.stream()
                        .filter(booking -> now.isAfter(booking.getStart()))
                        .min((booking1, booking2) -> booking2.getStart().compareTo(booking1.getStart())).orElse(null);

            }

            Booking nextBooking = null;
            int sizeNext = allBookings
                    .stream()
                    .filter(booking -> now.isBefore(booking.getStart()))
                    .collect(Collectors.toList()).size();
            if (sizeNext != 0) {
                nextBooking = allBookings
                        .stream()
                        .filter(booking -> now.isBefore(booking.getStart()))
                        .min(Comparator.comparing(Booking::getStart)).orElse(null);
            }

            if (lastBooking != null) {
                itemLastNextDTO.setLastBooking(mapper.map(lastBooking, BookingLastNextItemDto.class));
            } else itemLastNextDTO.setLastBooking(null);
            if (nextBooking != null) {
                itemLastNextDTO.setNextBooking(mapper.map(nextBooking, BookingLastNextItemDto.class));
            } else itemLastNextDTO.setNextBooking(null);


            List<Comment> comments = commentRepoJpa.findAllByItemOrderById(item);
            List<CommentDto> commentDtos = comments.stream()
                    .map(comment -> {
                        return mapper.map(comment, CommentDto.class);
                    })
                    .collect(Collectors.toList());
            itemLastNextDTO.setComments(commentDtos);

            itemLastNextDTOList.add(itemLastNextDTO);
        }
        return itemLastNextDTOList;
    }

    @Override
    public List<ItemDTO> searchByParamService(String text, int from, int size) {
        if (text == null || text.isEmpty()) {
            log.debug("Запрос не задан");
            return new ArrayList<>();
        }
        String textToLowerCase = text.toLowerCase();
        if ((from < 0 || size < 0 || (from == 0 && size == 0))) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "не правильный параметр пагинации");
        }
        Pageable pageable;

        if (text == null || text.isEmpty()) {
            log.debug("Вещь по запросу {} не найдена", text);
            return new ArrayList<>();
        } else {
            pageable = PageRequest.of(from / size, size);
            log.debug("Вещь по запросу {} найдена", text);
            return itemRepoJpa.searchByParam(textToLowerCase, pageable)
                    .stream()
                    .map(item -> {
                        return mapper.map(item, ItemDTO.class);
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public CommentDto addComment(int userId, int itemId, CommentDto commentDto) {
        if (commentDto.getContent().isBlank()) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST, "Комментарий не должен быть пустым");
        }
        User user = userRepoJpa.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException(HttpStatus.NOT_FOUND, "комментарий  к вещи с id = '" + itemId
                                + "' пользователем с id = " + userId + " ; нет информации о пользователе."));
        Item item = itemRepoJpa.findById(itemId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "комментарий к вещи с id = '" + itemId
                        + "' пользователем с id = '" + userId + "' - отсутствует запись о вещи."));
        List<Booking> bookings = item.getBookings();

        if (bookings
                .stream()
                .filter(booking -> (booking.getBooker().getId() == userId)
                        && !booking.getStart().isAfter(LocalDateTime.now())
                        && !booking.getStatus().equals(Status.REJECTED)
                )
                .collect(Collectors.toList()).size() == 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST,
                    "Комментировать может только арендатор вещи, с наступившим началом букинга и статусом НЕ REJECTED");
        }

        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        Comment commentToDto = commentRepoJpa.save(comment);
        return mapper.map(commentToDto, CommentDto.class);
    }
}
