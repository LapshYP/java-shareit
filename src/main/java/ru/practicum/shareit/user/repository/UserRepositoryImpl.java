package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DubleException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final HashMap<Integer, User> userList = new HashMap<>();
    private static int id = 1;

public HashMap<Integer, User> getUserList (){
    return userList;
}

    @Override
    public List <User> getAllUsers() {

        return userList.values().stream()
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public User createUserRepo(User user) {
        if (userList.values().stream().filter(user1 -> user1.getEmail().equals(user.getEmail())).count()>0) {
            throw new DubleException("Уже существует");
        } else {
            user.setId(id++);
            userList.put(user.getId(),user);
        }

        return user;
    }

    @SneakyThrows
    @Override
    public User getUserById(int id) {
         return userList.get(id);

    }

    @SneakyThrows
    @Override
    public User updateUserRepo(User user, int userId) {
        User updatedUser = getUserById(userId);
        if (user.getName()!=null) {
           updatedUser.setName(user.getName());
             }
        if (user.getEmail()!=null) {
            boolean hasEmail = userList.values().stream()
                    .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()) && user1.getId() != userId);
            if (hasEmail) {
                throw new DubleException("Такой емейл уже существует");
            }
            updatedUser.setEmail(user.getEmail());
        }
//        if (user.getEmail()!=null && user.getName()!=null) {
//            updatedUser.setEmail(user.getEmail());
//        }
//        if (user.getName()==null && user.getEmail() != null) {
//            throw new NotFoundException(HttpStatus.BAD_REQUEST," не правильный запрос");
//        }
        updatedUser.setId(userId);
        userList.put(userId,updatedUser);

        return updatedUser;
    }

    @Override
    public User deleteUserRepo(int userId) {
        userList.remove(userId);
        return getUserById(userId);
    }
}
