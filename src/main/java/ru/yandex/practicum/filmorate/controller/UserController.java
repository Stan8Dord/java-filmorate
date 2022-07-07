package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
public class UserController {
    private final UserStorage storage;
    private final UserService service;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserStorage storage, UserService service) {
        this.storage = storage;
        this.service = service;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("post: \n" + user);

        if (user.getName() == null || user.getName().equals(""))
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getLogin().contains(" ")) {
            log.error("не пройдена валидация данных нового пользователя \n" + user);
            throw new ValidationException("Некорректные данные нового пользователя!");
        } else {
            storage.createUser(user);
        }

        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("put: \n" + user);

        return storage.updateUser(user);
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        return storage.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long userId) {
        Optional<User> userOptional = storage.getUserById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Нет такого пользователя: id = " + userId);
        }

        return userOptional.get();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        service.removeFriend(userId, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> getUserFriends(@PathVariable("id") Long userId) {
        return service.getUserFriends(userId);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long otherId) {
        return service.getCommonFriends(userId, otherId);
    }
}
