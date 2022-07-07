package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    public User createUser(User user);

    public Collection<User> getAllUsers();

    public User updateUser(User user);

    public Optional<User> getUserById(Long id);
}
