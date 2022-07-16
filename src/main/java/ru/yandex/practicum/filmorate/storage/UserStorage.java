package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User createUser(User user);

    public List<User> getAllUsers();

    public User updateUser(User user);

    public User getUserById(Long id);

    public void addFriend(Long userId, Long friendId);

    public void removeFriend(Long userId, Long friendId);

    public List<Long> getCommonFriends(Long userId, Long otherId);

    public List<Long> getUserFriends(Long userId);
}
