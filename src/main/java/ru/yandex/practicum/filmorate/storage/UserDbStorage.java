package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("dbStorage")
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select \"id\" from \"users\"");

        while(userRows.next()) {
            long id = userRows.getLong("id");

            allUsers.add(getUserById(id));
        }

        return allUsers;
    }

    @Override
    public User getUserById(Long id) {
        User user = null;
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("select * from \"users\" where \"id\" = ?", id);

        if (userRow.next()) {
            Set<Long> userFriends = new HashSet<>();

            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select \"friend2_id\" from \"friends\" where \"friend1_id\" = ?", id);
            while (friendsRows.next()) {
                userFriends.add(friendsRows.getLong("friend2_id"));
            }
            String name = userRow.getString("name");
            if (name.isBlank())
                name = userRow.getString("login");

            user = new User(
                    userRow.getString("email"),
                    userRow.getString("login"),
                    name,
                    userRow.getDate("birthday").toLocalDate()
            );
            user.setId(id);
            user.setFriends(userFriends);
        }

        log.info("Найден пользователь: id = {} email = {}", id, user.getEmail());

        return user;
    }

    @Override
    public User createUser(User user) {
        String sql = "insert into \"users\"(\"email\", \"login\", \"name\", \"birthday\") "
                + "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement prst =
                    connection.prepareStatement(sql, new String[]{"id"});
            prst.setString(1, user.getEmail());
            prst.setString(2, user.getLogin());
            prst.setString(3, user.getName());
            prst.setDate(4, Date.valueOf(user.getBirthday()));
            return prst;
        }, keyHolder);

        user.setId(keyHolder.getKey().longValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "update \"users\" set " + "\"email\" = ?, \"login\" = ?, \"name\" = ?, \"birthday\" = ? "
                + "where \"id\" = ?";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String sql = "insert into \"friends\"(\"friend1_id\", \"friend2_id\") " + "values (?, ?)";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String sqlQuery = "delete from \"friends\" where \"friend1_id\" = ? and  \"friend2_id\" = ?";

        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<Long> getCommonFriends(Long userId, Long otherId) {
        List<Long> commonFriends = new ArrayList<>();

        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select f.\"friend2_id\" AS \"common_friend\" " +
                "from \"friends\" f JOIN (SELECT \"friend2_id\" FROM \"friends\" WHERE \"friend1_id\" = ?) f2 " +
                "ON f.\"friend2_id\" = f2.\"friend2_id\" WHERE f.\"friend1_id\" = ?", userId, otherId);

        while (friendsRows.next()) {
            commonFriends.add(friendsRows.getLong("common_friend"));
        }

        return commonFriends;
    }

    public List<Long> getUserFriends(Long userId) {
        List<Long> friends = new ArrayList<>();

        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select \"friend2_id\" from \"friends\" " +
                " WHERE \"friend1_id\" = ?", userId);

        while (friendsRows.next()) {
            friends.add(friendsRows.getLong("friend2_id"));
        }

        return friends;
    }
}
