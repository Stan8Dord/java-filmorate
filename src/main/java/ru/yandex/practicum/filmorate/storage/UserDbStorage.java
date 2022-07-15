package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from \"users\"");



        while(userRows.next()) {
            System.out.println("last row = " + userRows.isLast());
            long id = userRows.getLong("id");
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select \"friend2_id\" from \"friends\" where \"friend1_id\" = ?", id);
            Set<Long> userFriends = new HashSet<>();

            //System.out.println("Ch1: " + Arrays.stream(friendsRows.getMetaData().getColumnNames()).collect(Collectors.toList()));
            //System.out.println("Ch1: " + friendsRows.getMetaData().getColumnCount());

            while (friendsRows.next()) {
                userFriends.add(friendsRows.getLong("friend2_id"));
            }
            User user = new User(
                    id,
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate(),
                    userFriends
            );

            System.out.println("user = " + user.toString());
            allUsers.add(user);
        }
        return allUsers;
    }

    @Override
    public User getUserById(Long id) {
        User user = null;
        SqlRowSet userRow = jdbcTemplate.queryForRowSet("select * from \"users\" where \"id\" = ?", id);

        if (userRow.next()) {
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("select \"friend2_id\" from \"friends\" where \"friend1_id\" = ?", id);
            Set<Long> userFriends = new HashSet<>();
            if (friendsRows.next()) {
                userFriends.add(friendsRows.getLong("friend2_id"));
            }
            user = new User(
                    id,
                    userRow.getString("email"),
                    userRow.getString("login"),
                    userRow.getString("name"),
                    userRow.getDate("birthday").toLocalDate(),
                    userFriends
            );
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
}
