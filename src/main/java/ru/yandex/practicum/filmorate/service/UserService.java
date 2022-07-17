package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class UserService implements UserServiceInterface {
    @Autowired
    @Qualifier("dbStorage")
    UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        getUserById(friendId);
        getUserById(userId);

        storage.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);

        storage.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long otherId) {
        List<User> friends = new ArrayList<>();

        getUserById(userId);
        getUserById(otherId);

        for (Long id : storage.getCommonFriends(userId, otherId)) {
            friends.add(getUserById(id));
        }

        return friends;
    }

    @Override
    public List<User> getUserFriends(Long userId) {
        List<User> friends = new ArrayList<>();

        for (Long id : storage.getUserFriends(userId)) {
            friends.add(getUserById(id));
        }

        return friends;
    }

    @Override
    public User createUser(User user) {
        return storage.createUser(validUser(user));
    }

    @Override
    public User updateUser(User user) {
        Long id = user.getId();
        if (id < 0) {
            throw new UserNotFoundException("Нет такого пользователя id = " + id);
        }
        if (storage.getAllUsers().stream().anyMatch(u -> u.getId() == id))
            return storage.updateUser(validUser(user));
        else
            throw new ValidationException("Некорректные данные!");
    }

    @Override
    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        if (storage.getAllUsers().stream().anyMatch(u -> u.getId() == id))
            return storage.getUserById(id);
        else
            throw new UserNotFoundException("Нет такого пользователя: id = " + id);
    }

    @Override
    public User validUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getLogin().contains(" ")) {
            log.error("не пройдена валидация данных нового пользователя \n" + user);
            throw new ValidationException("Некорректные данные нового пользователя!");
        }

        return user;
    }
}
