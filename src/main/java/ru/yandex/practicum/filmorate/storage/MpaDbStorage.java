package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.ArrayList;
import java.util.List;

@Component("mpaDbStorage")
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<MpaRating> getAllMpa() {
        List<MpaRating> allMpa = new ArrayList<>();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select * from \"mpa_ratings\"");

        while (mpaRows.next()) {
            MpaRating mpa = new MpaRating(mpaRows.getInt("id"), mpaRows.getString("mpa_rating"));

            allMpa.add(mpa);
        }

        return allMpa;
    }

    public MpaRating getMpaById(int id) {
        MpaRating mpa = null;
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet("select * from \"mpa_ratings\" where \"id\" = ?", id);

        if(mpaRow.next()) {
            mpa = new MpaRating(mpaRow.getInt("id"), mpaRow.getString("mpa_rating"));
        }

        return mpa;
    }
}
