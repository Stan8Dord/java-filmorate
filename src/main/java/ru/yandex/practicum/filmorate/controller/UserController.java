package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static int lastId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("post: \n" + user);

        if (user.getName() == null || user.getName().equals(""))
            user.setName(user.getLogin());
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getLogin().contains(" ")) {
            log.error("не пройдена валидация данных нового пользователя \n" + user);
            throw new ValidationException("Некорректные данные нового пользователя!");
        } else {
            user.setId(lastId);
            users.put(lastId++, user);
        }

        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        log.info("put: \n" + user);

        if (users.keySet().contains(user.getId()))
            users.put(user.getId(), user);
        else
            throw new ValidationException("Некорректные данные!");

        return user;
    }

    @GetMapping("/users")
    public Collection<User> getAllUsers() {
        if (!users.isEmpty())
            return users.values();
        else
            return null;
    }
}
