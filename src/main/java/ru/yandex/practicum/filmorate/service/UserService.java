package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {
    UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long userId, Long friendId) {
        checkUserById(userId);
        checkUserById(friendId);

        User user = storage.getUserById(userId).get();
        User friend = storage.getUserById(friendId).get();

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        checkUserById(userId);
        checkUserById(friendId);

        User user = storage.getUserById(userId).get();
        User friend = storage.getUserById(friendId).get();

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> getCommonFriends(Long userId, Long otherId) {
        List<User> friends = new ArrayList<>();

        checkUserById(userId);
        checkUserById(otherId);

        Set<Long> userFriends = storage.getUserById(userId).get().getFriends();
        Set<Long> otherFriends = storage.getUserById(otherId).get().getFriends();

        for (Long friend : userFriends) {
            if (otherFriends.contains(friend))
                friends.add(storage.getUserById(friend).get());
        }

        return friends;
    }

    public void checkUserById(Long userId) {
        Optional<User> userOptional = storage.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Нет такого пользователя: id = " + userId);
        }
    }

    public Collection<User> getUserFriends(Long userId) {
        checkUserById(userId);
        List<User> friends = new ArrayList<>();

        Set<Long> idFriends = storage.getUserById(userId).get().getFriends();

        for (Long id : idFriends) {
            friends.add(storage.getUserById(id).get());
        }

        return friends;
    }
}
