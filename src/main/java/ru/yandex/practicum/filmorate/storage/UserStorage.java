package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    User getUserById(Long id);

    void addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<Long> getCommonFriends(Long userId, Long otherId);

    List<Long> getUserFriends(Long userId);
}
