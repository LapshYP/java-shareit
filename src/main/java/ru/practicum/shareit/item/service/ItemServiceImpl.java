package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingLastNextItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repossitory.BookingRepoJpa;
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
    private final BookingRepoJpa bookingRepoJpa;
    private final ItemRepoJpa itemRepoJpa;
    private final UserRepoJpa userRepoJpa;
    private final CommentRepoJpa commentRepoJpa;
//    private final ItemMapper itemMapper
//            = Mappers.getMapper(ItemMapper.class);
//    private final ItemLastNextDtoMapper itemLastNextDtoMapper
//            = Mappers.getMapper(ItemLastNextDtoMapper.class);
//    private final BookingMapper bookingMapper
//            = Mappers.getMapper(BookingMapper.class);

    //    private final CommentMapper commentMapper
//            = Mappers.getMapper(CommentMapper.class);
    private final ModelMapper mapper = new ModelMapper();

    @SneakyThrows
    @Override
    public ItemDTO createService(ItemDTO itemDTO, int userId) {
        //     Item item = itemMapper.itemDTOToItem(itemDTO);
        Item item = mapper.map(itemDTO, Item.class);

        if (item.getAvailable() == null) {
            log.error("Вещь с именем = {} и описанием {} не доступна", item.getName(), item.getDescription());
            throw new BadRequestException(HttpStatus.BAD_REQUEST, item.getName() + " не доступна");
        }
        userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Пользователь с id = '" + userId + "' не найден"));
    //   itemRepoJpa.findItemByNameAndDescription(item.getName(), item.getDescription()).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь с именем '" + item.getName() + "' не найденf"));

//        if (itemRepoJpa.findAll()
//                .stream()
//                .filter(item1 -> (item1.getName().equals(item.getName())
//                        && item1.getDescription().equals(item.getDescription()))).count() > 0) {
//            log.error("Вещь с именем = {} и описанием {} уже создана", item.getName(), item.getDescription());
//            throw new DubleException(item.getName() + " уже создана");
//        }

        item.setOwner(userRepoJpa.getReferenceById(userId));
        log.debug("Вещь с именем = {} и описанием {} создана", item.getName(), item.getDescription());
        Item createdItem = itemRepoJpa.save(item);
        // ItemDTO createdItemDTO = itemMapper.itemToItemDTO(createdItem);
        ItemDTO createdItemDTO = mapper.map(createdItem, ItemDTO.class);
        return createdItemDTO;
    }

    @Override
    public ItemDTO updateService(ItemDTO itemDTO, int itemId, int userId) {
        //   Item item = itemMapper.itemDTOToItem(itemDTO);
        Item item = mapper.map(itemDTO, Item.class);

//        boolean isRightItem = itemRepoJpa.getById(itemId) != null ? true : false;
//        if (!isRightItem) {
//            log.error("Вещь с именем = {} и индификатором {} не существует", item.getName(), itemId);
//            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь c id = '" + itemId + " не существует");
//        }
        itemRepoJpa.findById(itemId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь c id = '" + item.getId() + "' не существует"));
//        boolean isRightUser = itemRepoJpa.getById(itemId).getOwner().getId() == userId ? true : false;
//        if (!isRightUser) {
//            log.error("Вещь с именем = {} и описанием {} не может быть обновлена", item.getName(), userId);
//            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не может быть обновлена этим пользователем");
//        }
        userRepoJpa.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь не может быть обновлена этим пользователем id = '" + userId + "' "));

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
        // ItemDTO updatedItemDTO = itemMapper.itemToItemDTO(updatedItem);
        ItemDTO updatedItemDTO = mapper.map(updatedItem, ItemDTO.class);
        return updatedItemDTO;
    }

    @Override
    public ItemLastNextDTO getByOwnerIdService(int itemId, int userId) {
//        if (!(userRepoJpa.findAll().stream().filter(user -> user.getId() == userId).count() > 0)) {
//            log.error("Пользователя с id= {} нет в базе данных", userId);
//            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных");
//        }
//        if (!(itemRepoJpa.findAll().stream().filter(item -> item.getId()==itemId).count() >0)) {
//            log.error("Вещи с id= {} нет в базе данных", itemId);
//            throw new NotFoundException(HttpStatus.NOT_FOUND, "Вещь с id  = '" + itemId + " нет в базе данных");
//        }

        Item item = itemRepoJpa.findById(itemId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Вещь с id  = '" + itemId + " нет в базе данных"));

        User owner = userRepoJpa.findById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных"));

        List<Booking> allBookings = item.getBookings();

        Booking lastBooking = null;
        Booking nextBooking = null;
        LocalDateTime now = LocalDateTime.now();

        int ownerId = item.getOwner().getId();
        if (ownerId == userId && allBookings != null) {
            //   allBookings.sort(Comparator.comparing(Booking::getStart));

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
        //ItemLastNextDTO itemLastNextDTO = itemLastNextDtoMapper.itemToItemLastNextDTO(item);
        ItemLastNextDTO itemLastNextDTO = mapper.map(item, ItemLastNextDTO.class);
        //  itemLastNextDTO.setLastBooking(bookingMapper.bookingToBookingLastNextItemDto(lastBooking));
        if (lastBooking != null && lastBooking.getStatus() != Status.REJECTED) {
            itemLastNextDTO.setLastBooking(mapper.map(lastBooking, BookingLastNextItemDto.class));
        } else itemLastNextDTO.setLastBooking(null);

        //  itemLastNextDTO.setNextBooking(bookingMapper.bookingToBookingLastNextItemDto(nextBooking));
        if (nextBooking != null && nextBooking.getStatus() != Status.REJECTED) {
            itemLastNextDTO.setNextBooking(mapper.map(nextBooking, BookingLastNextItemDto.class));
        } else itemLastNextDTO.setNextBooking(null);


        List<Comment> comments = item.getComments();
        List<CommentDto> commentDtoForResponse = comments
                .stream()
                //   .map(commentMapper::commentToCommentDto)
                .map(comment -> {
                    return mapper.map(comment, CommentDto.class);
                })
                .collect(Collectors.toList());
        itemLastNextDTO.setComments(commentDtoForResponse);

        log.debug("Вещь с id = {} созданная {} просмотрена", itemId, userId);
        return itemLastNextDTO;
    }

    @Override
    public List<ItemLastNextDTO> getByBookerIdService(int userId) {
//        if (!(userRepoJpa.findAll().stream().filter(user -> user.getId() == userId).count() > 0)) {
//            log.error("Пользователя с id= {} нет в базе данных", userId);
//            throw new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных");
//        }
        log.debug("Список всех вещей просмотрен");
        User booker = userRepoJpa.findById(userId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "Пользователя с id  = '" + userId + " нет в базе данных"));

        List<Item> items = itemRepoJpa.findAllByOwnerOrderById(booker);
        List<ItemLastNextDTO> itemLastNextDTOList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Item item : items) {
            //     ItemLastNextDTO itemLastNextDTO = itemLastNextDtoMapper.itemToItemLastNextDTO(item);
            ItemLastNextDTO itemLastNextDTO = mapper.map(item, ItemLastNextDTO.class);
            List<Booking> allBookings = item.getBookings();
            allBookings.sort(Comparator.comparing(Booking::getStart));

            Booking lastBooking = null;

            int sizeLast = allBookings.stream()
                    .filter(booking -> now.isAfter(booking.getStart()))
                    .collect(Collectors.toList()).size();
            if (sizeLast != 0) {
                lastBooking = allBookings.stream()
                        .filter(booking -> now.isAfter(booking.getStart())
                                //     && booking.getBooker().getId() == userId
                        )
                        .collect(Collectors.toList()).get(0);
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
                        .collect(Collectors.toList()).get(0);
            }

            //  itemLastNextDTO.setLastBooking(bookingMapper.bookingToBookingLastNextItemDto(lastBooking));
            if (lastBooking != null) {
                itemLastNextDTO.setLastBooking(mapper.map(lastBooking, BookingLastNextItemDto.class));
            } else itemLastNextDTO.setLastBooking(null);
            // itemLastNextDTO.setNextBooking(bookingMapper.bookingToBookingLastNextItemDto(nextBooking));
            if (nextBooking != null) {
                itemLastNextDTO.setNextBooking(mapper.map(nextBooking, BookingLastNextItemDto.class));
            } else itemLastNextDTO.setNextBooking(null);


            List<Comment> comments = commentRepoJpa.findAllByItemOrderById(item);
            List<CommentDto> commentDtos = comments.stream()
                    //  .map(commentMapper::commentToCommentDto).collect(Collectors.toList());
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
                    //    .map(itemMapper::itemToItemDTO)
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
                        + "' пользователем с id = " + userId + " ; отсутствует запись о пользователе."));
        Item item = itemRepoJpa.findById(itemId).orElseThrow(() ->
                new NotFoundException(HttpStatus.NOT_FOUND, "комментарий к вещи с id = '" + itemId
                        + "' пользователем с id = '" + userId + "' - отсутствует запись о вещи."));
        List<Booking> bookings = item.getBookings();
//        List<Booking> bookings1 = bookings
//                .stream()
//                .filter(booking -> (booking.getBooker().getId() == userId)
//                && booking.getEnd().isBefore(LocalDateTime.now())
//                )
//                .collect(Collectors.toList());
        if (bookings
                .stream()
                .filter(booking -> (booking.getBooker().getId() == userId)
                        && booking.getEnd().isBefore(LocalDateTime.now())
                )
                .collect(Collectors.toList()).size() == 0) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST,
                    "Комментировать может только арендатор вещи");
        }


        // Comment comment = commentMapper.commentDtoToComment(commentDto);
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());

        //  return commentMapper.commentToCommentDto(commentRepoJpa.save(comment));
        Comment commentToDto = commentRepoJpa.save(comment);
        return mapper.map(commentToDto, CommentDto.class);
    }
}
