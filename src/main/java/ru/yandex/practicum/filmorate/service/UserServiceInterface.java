package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserServiceInterface {
    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<User> getCommonFriends(Long userId, Long otherId);

    List<User> getUserFriends(Long userId);

    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User validUser(User user);
}
