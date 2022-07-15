package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("dbFStorage")
@Primary
public class FilmDbStorage implements  FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "insert into \"films\"(\"name\", \"description\", \"release_date\", \"duration\", " +
                "\"mpa_rating_id\") " + "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement prst =
                    connection.prepareStatement(sql, new String[]{"id"});
            prst.setString(1, film.getName());
            prst.setString(2, film.getDescription());
            prst.setDate(3, Date.valueOf(film.getReleaseDate()));
            prst.setLong(4, film.getDuration());
            prst.setString(5, "");
            return prst;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from \"films\"");

        while(filmRows.next()) {
            long id = filmRows.getLong("id");
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select \"user_id\" from \"likes\" where \"film_id\" = ?", id);
            Set<Long> filmLikes = new HashSet<>();

            while (likesRows.next()) {
                filmLikes.add(likesRows.getLong("user_id"));
            }

            SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select \"genre_id\" from \"film_genres\" where \"film_id\" = ?", id);
            Set<Integer> filmGenres = new HashSet<>();

            while (genresRows.next()) {
                filmGenres.add(genresRows.getInt("genre_id"));
            }

            Film film  = new Film(
                    id,
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getLong("duration"),
                    filmRows.getInt("mpa_rating_id"),
                    filmLikes,
                    filmGenres
            );

            allFilms.add(film);
        }
        return allFilms;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update \"films\" set " + "\"name\" = ?, \"description\" = ?, \"release_date\" = ?" +
                ", \"duration\" = ?, \"mpa_rating_id\" = ?" + "where \"id\" = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpaRating(),
                film.getId());

        return film;
    }


    @Override
    public Film getFilmById(Long id) {
        Film film = null;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from \"films\" where \"id\" = ?", id);

        if(filmRows.next()) {
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("select \"user_id\" from \"likes\" where \"film_id\" = ?", id);
            Set<Long> filmLikes = new HashSet<>();

            while (likesRows.next()) {
                filmLikes.add(likesRows.getLong("user_id"));
            }

            SqlRowSet genresRows = jdbcTemplate.queryForRowSet("select \"genre_id\" from \"film_genres\" where \"film_id\" = ?", id);
            Set<Integer> filmGenres = new HashSet<>();

            while (genresRows.next()) {
                filmGenres.add(genresRows.getInt("genre_id"));
            }
            film = new Film(
                    id,
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("release_date").toLocalDate(),
                    filmRows.getLong("duration"),
                    filmRows.getInt("mpa_rating_id"),
                    filmLikes,
                    filmGenres
            );
        }

        return film;
    }
}
