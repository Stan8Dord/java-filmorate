package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component("genreDbStorage")
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        List<Genre> allGenres = new ArrayList<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("select * from \"genres\"");

        while(genreRows.next()) {
            Genre genre = new Genre(genreRows.getInt("id"), genreRows.getString("genre"));

            allGenres.add(genre);
        }

        return allGenres;
    }

    public Genre getGenreById(int id) {
        Genre genre = null;
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet("select * from \"genres\" where \"id\" = ?", id);

        if(genreRow.next()) {
            genre = new Genre(genreRow.getInt("id"), genreRow.getString("genre"));
        }

        return genre;
    }
}
