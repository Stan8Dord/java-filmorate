package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    @Autowired
    @Qualifier("dbStorage")
    UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long userId, Long friendId) {
        getUserById(friendId);
        getUserById(userId);

        //User user = storage.getUserById(userId);
        //user.addFriend(friendId);

        storage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        getUserById(userId);
        getUserById(friendId);

        //User user = storage.getUserById(userId);
        //User friend = storage.getUserById(friendId);
        //user.getFriends().remove(friendId);
        //friend.getFriends().remove(userId);

        storage.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        List<User> friends = new ArrayList<>();

        getUserById(userId);
        getUserById(otherId);

        //Set<Long> userFriends = storage.getUserById(userId).getFriends();
        //Set<Long> otherFriends = storage.getUserById(otherId).getFriends();
        //for (Long friend : userFriends) {
        //    if (otherFriends.contains(friend))
        //        friends.add(storage.getUserById(friend));
        //}

        for (Long id : storage.getCommonFriends(userId, otherId)) {
            friends.add(getUserById(id));
        }

        return friends;
    }

    public List<User> getUserFriends(Long userId) {
        List<User> friends = new ArrayList<>();

        //Set<Long> idFriends = storage.getUserById(userId).getFriends();
        //for (Long id : idFriends) {
        //    friends.add(storage.getUserById(id));
        //}

        for (Long id : storage.getUserFriends(userId)) {
            friends.add(getUserById(id));
        }

        return friends;
    }

    public User createUser(User user) {
        return storage.createUser(validUser(user));
    }

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

    public List<User> getAllUsers() {
        return storage.getAllUsers();
    }

    public User getUserById(Long id) {
        if (storage.getAllUsers().stream().anyMatch(u -> u.getId() == id))
            return storage.getUserById(id);
        else
            throw new UserNotFoundException("Нет такого пользователя: id = " + id);
    }

    public User validUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getLogin().contains(" ")) {
            UserController.log.error("не пройдена валидация данных нового пользователя \n" + user);
            throw new ValidationException("Некорректные данные нового пользователя!");
        }

        return user;
    }
}
