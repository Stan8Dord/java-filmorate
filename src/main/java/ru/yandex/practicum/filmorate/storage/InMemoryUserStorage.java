package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("imMemoryStorage")
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
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {}

    @Override
    public void removeFriend(Long userId, Long friendId){}

    @Override
    public List<Long> getCommonFriends(Long userId, Long otherId){
        return null;
    }

    @Override
    public List<Long> getUserFriends(Long userId){
        return null;
    }
}
