package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private static Long lastId = 1L;

    @Override
    public User createUser(User user) {
        user.setId(lastId);
        users.put(lastId++, user);

        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        if (id < 0) {
            throw new UserNotFoundException("Нет такого пользователя id = " + id);
        }
        if (users.keySet().contains(id))
            users.put(id, user);
        else
            throw new ValidationException("Некорректные данные!");

        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        }

        return Optional.empty();
    }
}
